package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.enums.Sort;
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

    List<EventShortDto> getEvents(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, boolean onlyAvailable, Sort sort, int from, int size);

    List<EventFullDto> getUsersEvent(List<Long> usersId, List<EventStatus> states, List<Long> categoriesId,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateDto dto);

    EventFullDto getEvent(Long id);
}
