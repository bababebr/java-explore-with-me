package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.event.NewEventDto;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.interfaces.IEventService;

import java.util.List;

@Service
public class EventService implements IEventService {

    private final EventRepository repository;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EventShortDto> getUserOwnEvents(Long userId) {
        return null;
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
}
