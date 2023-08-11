package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.models.event.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.service.interfaces.IEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService implements IEventService {

    private final EventRepository repository;
    private final ParticipantRepository participantRepository;
    @Autowired
    public EventService(EventRepository repository, ParticipantRepository participantRepository) {
        this.repository = repository;
        this.participantRepository = participantRepository;
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
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto) {
        return null;
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, boolean onlyAvailable, Sort sort, int from, int size) {

        return repository.findEvents(text, categories, paid).stream().map(EventMapper::eventToShort)
                .collect(Collectors.toList());

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
}
