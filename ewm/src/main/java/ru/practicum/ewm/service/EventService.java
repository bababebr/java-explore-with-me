package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.event.*;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.*;
import ru.practicum.ewm.service.interfaces.IEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {

    private final EventRepository repository;
    private final ParticipantRepository participantRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public EventService(EventRepository repository, ParticipantRepository participantRepository,
                        CategoryRepository categoryRepository, UserRepository userRepository,
                        LocationRepository locationRepository) {
        this.repository = repository;
        this.participantRepository = participantRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<EventShortDto> getUserOwnEvents(Long userId, int from, int size) {
        List<Event> events = repository.findAllByInitiatorId(userId, PageRequest.of(from, size));
        return setEventShortDto(events);
    }

    @Override
    public EventFullDto addEvent(Long userInd, NewEventDto newEventDto) {
        User user = userRepository.findById(userInd).orElseThrow(() -> new NoSuchElementException());

        Location rawLocation = Location.create(0L,
                repository.getNextEventId().orElse(1L),
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon());

        Location location = locationRepository.save(rawLocation);

        Category category;
        if (newEventDto.getCategoryId() == null) {
            newEventDto.setCategoryId(1L);
        }
        category = categoryRepository.findById(newEventDto.getCategoryId()).orElseThrow(() -> new NoSuchElementException());

        Event savedEvent = repository.save(EventMapper.newEventToEvent(newEventDto, category, user, location));

        return EventMapper.eventToFull(savedEvent);
    }

    @Override
    public EventFullDto getEventFullInfo(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto) {
        Event oldEvent = repository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        if (!(oldEvent.getInitiator().getId() == userId)) {
            throw new IllegalStateException();
        }
        update(oldEvent, dto);

        return EventMapper.eventToFull(repository.save(oldEvent));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, Sort sort, int from, int size) {

        if (text == null) {
            return new ArrayList<>();
        }

        String sortType;
        if (sort.equals(Sort.VIEWS)) {
            sortType = "e.views";
        } else {
            sortType = "e.event_date";
        }
        List<Event> events;
        if (onlyAvailable) {
            if (rangeStart == null) {
                events = repository.findEventsIsAvailableWithoutRange(text, categories, paid, sortType);
            } else {
                events = repository.findEventsIsAvailableWithRange(text, categories, paid, rangeStart,
                        rangeEnd, sortType);
            }
        } else {
            if (rangeStart == null) {
                events = repository.findEventsWithoutRange(text, categories, paid, sortType);
            } else {
                events = repository.findEventsWithRange(text, categories, paid, rangeStart, rangeEnd, sortType);
            }
        }
        return events.stream().map(EventMapper::eventToShort).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getUsersEvent(List<Long> usersId, List<String> states, List<Long> categoriesId,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return null;
    }

    private List<EventShortDto> setEventShortDto(List<Event> events) {
        List<EventShortDto> returnList = new ArrayList<>();
        for (Event e : events) {
            int participantCount = participantRepository.countAllByEventIdAndStatus(e.getId(), ParticipantRequestStatus.CONFIRMED);
            /**
             * TODO view count calc
             */
            int viewCount = 0;
            EventShortDto shortDto = EventMapper.eventToShort(e);
            shortDto.setConfirmedRequest(participantCount);
            returnList.add(shortDto);
        }
        return returnList;
    }

    private void update(Event event, EventUpdateDto dto) {
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategoryId() != null) {
            event.setCategory(categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException()));
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
        if (dto.getState() != null) {
            event.setState(dto.getState());
        }
    }
}
