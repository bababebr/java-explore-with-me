package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.models.event.*;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
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

    @Autowired
    public EventService(EventRepository repository, ParticipantRepository participantRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.participantRepository = participantRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<EventShortDto> getUserOwnEvents(Long userId, int from, int size) {
        List<Event> events = repository.findAllByInitiatorId(userId, PageRequest.of(from, size));
        return setEventShortDto(events);
    }

    @Override
    public EventFullDto addEvent(Long userInd, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEventFullInfo(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, EventFullDto dto) {
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

    private void update(Event event, EventFullDto dto) {
        event.setEventDate(dto.getEventDate());
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(categoryRepository.findById(dto.getCategoryDto().getId().longValue()).orElseThrow(() -> new NoSuchElementException()));
        event.setDescription(dto.getDescription());
        event.setDescription(dto.getDescription());
        event.setTitle(dto.getTitle());
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setState(dto.getState());
    }
}
