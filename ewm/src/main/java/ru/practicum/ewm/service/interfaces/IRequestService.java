package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.request.eventRequest.RequestDto;
import ru.practicum.ewm.models.request.eventRequest.RequestUpdateDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestUpdateDto;

import java.util.List;

public interface IRequestService {
    /**
     * Event requests
     */
    RequestDto getEventRequest(Long userId, Long eventId);
    RequestDto updateEventRequest(Long userId, Long eventId, RequestUpdateDto dto);

    /**
     * Participants requests
     */
    List<ParticipantRequestDto> getUserRequest(Long userId);

    ParticipantRequestDto addUserRequest(Long userId, Long eventId);

    ParticipantRequestDto cancelRequest(Long userId, Long requestId);

    ParticipantRequestDto updateRequest(Long userId, Long eventId, ParticipantRequestDto dto);

    ParticipantRequestDto getRequest(Long userId, Long evenId);

    List<ParticipantRequestDto> confirmRequest(Long userId, Long eventId, ParticipantRequestUpdateDto requestUpdateDto);
}
