package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;
import ru.practicum.ewm.models.comments.UpdateCommentDto;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.event.NewEventDto;
import ru.practicum.ewm.models.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.participantRequest.ParticipantRequestUpdateDto;
import ru.practicum.ewm.service.CommentService;
import ru.practicum.ewm.service.interfaces.ICommentService;
import ru.practicum.ewm.service.interfaces.IEventService;
import ru.practicum.ewm.service.interfaces.IRequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class PrivateController {

    private final IEventService eventService;
    private final IRequestService requestService;

    private final ICommentService commentService;

    @Autowired
    public PrivateController(IEventService eventService, IRequestService requestService,
                             CommentService commentService) {
        this.eventService = eventService;
        this.requestService = requestService;
        this.commentService = commentService;
    }

    /**
     * Private: Event
     */
    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getUserOwnEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
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

    /**
     * Private: Event requests
     */
    @GetMapping("/{userId}/requests")
    public List<ParticipantRequestDto> getUserRequests(@PathVariable Long userId) {
        return requestService.getUserRequest(userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipantRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getUserEventParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public Map<String, List<ParticipantRequestDto>> updateEventRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                                       @Valid @RequestBody ParticipantRequestUpdateDto requestUpdateDto) {
        return requestService.changeRequestStatus(userId, eventId, requestUpdateDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantRequestDto addUserRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.addUserRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipantRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    /**
     * Comments
     */
    @PostMapping("/{userId}/comments/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable Long userId, @PathVariable Long eventId,
                                  @Valid @RequestBody NewCommentDto dto) {
        return commentService.add(userId, eventId, dto);
    }

    @PatchMapping("/{userId}/comments/")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @Valid @RequestBody UpdateCommentDto dto) {
        return commentService.update(userId, dto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public CommentDto deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return commentService.delete(userId, commentId);
    }

    @GetMapping("/{userId}/comments/event/{eventId}")
    public List<CommentDto> getUsersComments(@PathVariable Long userId, @PathVariable Long eventId) {
        return commentService.getUserComments(userId, eventId);
    }

    @GetMapping("{userId}/comments/{commentId}")
    public CommentDto getUserComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return commentService.getUserComment(userId, commentId);
    }
}
