package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.event.NewEventDto;

import java.util.List;

public interface IEventService {

    List<EventShortDto> getUserOwnEvents(Long userId);
    EventFullDto addEvent(Long userInd, NewEventDto newEventDto);
    EventFullDto getEventFullInfo(Long userId, Long eventId);
    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto);

}
