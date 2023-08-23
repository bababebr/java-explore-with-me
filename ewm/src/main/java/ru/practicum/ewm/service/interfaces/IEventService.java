package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.event.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {

    List<EventShortDto> getUserOwnEvents(Long userId, int from, int size);

    EventFullDto addEvent(Long userInd, NewEventDto newEventDto);

    EventFullDto getEventFullInfo(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateDto dto);

    List<EventShortDto> getEvents(EventSearchDto dto, int from, int size,
                                  HttpServletRequest request);

    List<EventFullDto> getUsersEvent(List<Long> usersId, List<EventStatus> states, List<Long> categoriesId,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateDto dto);

    EventFullDto getEvent(Long id, HttpServletRequest request);
}
