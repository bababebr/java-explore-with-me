package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.request.eventRequest.RequestDto;
import ru.practicum.ewm.models.request.eventRequest.RequestUpdateDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;

import java.util.List;

public interface IRequestService {
    /**
     * Event requests
     */
    RequestDto get(Long userId, Long eventId);
    RequestDto updateRequest(Long userId, Long eventId, RequestUpdateDto dto);

    /**
     * Participants requests
     */
    List<ParticipantRequestDto> getUserRequest(Long userId);

    ParticipantRequestDto addUserRequest(Long userId, Long eventId);

    ParticipantRequestDto cancelRequest(Long userId, Long requestId);
}
