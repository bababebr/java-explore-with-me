package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.event.NewEventDto;
import ru.practicum.ewm.models.request.RequestDto;
import ru.practicum.ewm.models.request.RequestUpdateDto;
import ru.practicum.ewm.service.interfaces.IEventService;
import ru.practicum.ewm.service.interfaces.IRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class PrivateController {

    private final IEventService eventService;
    private final IRequestService requestService;

    @Autowired
    public PrivateController(IEventService eventService, IRequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    /**
    Private: Event
     */
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId) {
        return eventService.getUserOwnEvents(userId);
    }

    @PostMapping("/{userId}/events/")
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventFullInfo(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventFullInfo(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                    @Valid @RequestBody EventUpdateDto dto) {
        return eventService.updateEvent(userId, eventId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public RequestDto getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.get(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestDto updateEventRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                    @Valid @RequestBody RequestUpdateDto requestUpdateDto) {
        return requestService.updateRequest(userId, eventId, requestUpdateDto);
    }

    /**
     * Private: Event requests
     */
    @GetMapping("/{userId}/requests")
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequest(userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto addUserRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
