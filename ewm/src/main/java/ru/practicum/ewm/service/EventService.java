package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.comparators.EventSortByDate;
import ru.practicum.ewm.comparators.EventSortByViews;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.event.*;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.*;
import ru.practicum.ewm.service.interfaces.IEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService implements IEventService {

    private final EventRepository repository;
    private final ParticipantRepository participantRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;

    @Autowired
    public EventService(EventRepository repository, ParticipantRepository participantRepository,
                        CategoryRepository categoryRepository, UserRepository userRepository,
                        LocationRepository locationRepository, StatsClient statsClient) {
        this.repository = repository;
        this.participantRepository = participantRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.statsClient = statsClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserOwnEvents(Long userId, int from, int size) {
        List<Event> events = repository.findAllByInitiatorId(userId, PageRequest.of(from, size));
        return setEventShortDto(events);
    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = repository.getEventByIdAndState(id, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));
        Integer confirmedRequests = getConfirmedRequests(id);
        int views = updateHits(request, event.getPublishedDate(), LocalDateTime.now());
        return EventMapper.eventToFull(event, confirmedRequests, views);
    }

    @Override
    public EventFullDto addEvent(Long userInd, NewEventDto newEventDto) {
        User user = userRepository.findById(userInd).orElseThrow(NoSuchElementException::new);
        Duration duration = Duration.between(LocalDateTime.now(), newEventDto.getEventDate());
        if (duration.toHours() < 2) {
            throw new ValidationException("Event date is invalid.");
        }

        Location rawLocation = Location.create(0L,
                repository.getNextEventId().orElse(1L),
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon());
        Location location = locationRepository.save(rawLocation);

        Category category;
        if (newEventDto.getCategory() == null) {
            newEventDto.setCategory(1L);
        }
        category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NoSuchElementException());

        Event savedEvent = repository.save(EventMapper.newEventToEvent(newEventDto, category, user, location));
        Integer confirmedRequests = getConfirmedRequests(savedEvent.getId());

        return EventMapper.eventToFull(savedEvent, confirmedRequests, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventFullInfo(Long userId, Long eventId) {
        int confirmedRequests = participantRepository.countAllByEventIdAndStatusIn(eventId,
                List.of(ParticipantRequestStatus.CONFIRMED));
        Event event = repository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NoSuchElementException("Users not found"));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new IllegalStateException(String.format("USER with ID=%s not an owner", userId));
        }
        return EventMapper.eventToFull(event, confirmedRequests, 0);
    }

    @Override
    public List<EventShortDto> getEvents(EventSearchDto dto, int from, int size,
                                         HttpServletRequest request) {
        int views = updateHits(request, dto.getStart(), dto.getEnd());
        if (dto.getText() == null) {
            return new ArrayList<>();
        }
        if (dto.getStart() != null && dto.getEnd().isBefore(dto.getStart())) {
            throw new ValidationException("End date can't be before start");
        }

        Comparator<EventShortDto> comparator;
        switch (dto.getSort()) {
            case VIEWS:
                comparator = new EventSortByViews();
                break;
            default:
                comparator = new EventSortByDate();
        }
        List<Event> events = repository.getEvents(dto.getCategories(), dto.getOnlyAvailable(), dto.getPaid(),
                dto.getText(), dto.getStart(), dto.getEnd(), PageRequest.of(from, size));
        return events.stream().map(e -> EventMapper.eventToShort(e, views)).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getUsersEvent(List<Long> usersId, List<EventStatus> states, List<Long> categoriesId,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<Event> events = repository.getUserEvents(usersId, states, categoriesId, rangeStart, rangeEnd,
                PageRequest.of(from, size));
        List<EventFullDto> returnList = new ArrayList<>();
        for (Event event : events) {
            Integer confirmedRequests = getConfirmedRequests(event.getId());
            returnList.add(EventMapper.eventToFull(event, confirmedRequests, 0));
        }
        return returnList;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto) {
        Event oldEvent = repository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        if (oldEvent.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalStateException("Can't update cancelled or published event.");
        }
        validateEventAdminUpdate(oldEvent, dto);
        if (!(Objects.equals(oldEvent.getInitiator().getId(), userId))) {
            throw new IllegalStateException();
        }
        Event event = update(oldEvent, dto);
        Integer confirmedRequests = getConfirmedRequests(eventId);
        return EventMapper.eventToFull(event, confirmedRequests, 0);
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateDto dto) {
        Event event = repository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        validateEventAdminUpdate(event, dto);
        update(event, dto);
        Integer confirmedRequests = getConfirmedRequests(eventId);
        return EventMapper.eventToFull(event, confirmedRequests, 0);
    }

    private Event update(Event event, EventUpdateDto dto) {
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException(String
                            .format("Category with ID=%s not found", dto.getCategoryId()))));
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventStatus.PUBLISHED);
                    event.setPublishedDate(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    event.setState(EventStatus.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventStatus.PENDING);
                    break;
            }
        }
        return repository.save(event);
    }

    private void validateEventAdminUpdate(Event event, EventUpdateDto dto) {
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(event.getCreatedOn().minusHours(2))) {
            throw new ValidationException("Event date cannot be earlier than creation date.");
        }
        if (event.getState().equals(EventStatus.PUBLISHED) && dto.getStateAction().equals(StateAction.REJECT_EVENT)) {
            throw new IllegalStateException("Can't cancel published event.");
        }
        if (event.getState().equals(EventStatus.PUBLISHED) && dto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new IllegalStateException("Event already published.");
        }
        if (event.getState().equals(EventStatus.CANCELED) && dto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            throw new IllegalStateException("Can't publish cancelled event.");
        }
    }

    private int getConfirmedRequests(Long eventId) {
        return participantRepository.countAllByEventIdAndStatusIn(eventId,
                List.of(ParticipantRequestStatus.CONFIRMED, ParticipantRequestStatus.PENDING));
    }

    private List<EventShortDto> setEventShortDto(List<Event> events) {
        List<EventShortDto> returnList = new ArrayList<>();
        for (Event e : events) {
            int participantCount = participantRepository.countAllByEventIdAndStatus(e.getId(), ParticipantRequestStatus.CONFIRMED);
            EventShortDto shortDto = EventMapper.eventToShort(e, 0);
            shortDto.setConfirmedRequests(participantCount);
            returnList.add(shortDto);
        }
        return returnList;
    }

    private int updateHits(HttpServletRequest request, LocalDateTime start, LocalDateTime end) {
        statsClient.post(HitDto.create("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        ResponseEntity<Object> dto = statsClient.getHitsCount(start, end, List.of(request.getRequestURI()), true);
        return Integer.parseInt(dto.getBody().toString());
    }
}
