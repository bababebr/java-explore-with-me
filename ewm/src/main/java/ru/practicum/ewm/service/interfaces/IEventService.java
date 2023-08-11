package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.event.NewEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {

    List<EventShortDto> getUserOwnEvents(Long userId, int from, int size);
    EventFullDto addEvent(Long userInd, NewEventDto newEventDto);
    EventFullDto getEventFullInfo(Long userId, Long eventId);
    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto);

    List<EventShortDto> getEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                   LocalDateTime rangeEnd, boolean onlyAvailable, int from, int size);
}
