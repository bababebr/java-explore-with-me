package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestUpdateDto;

import java.util.List;

public interface IRequestService {
    /**
     * Participants requests
     */
    List<ParticipantRequestDto> getUserRequest(Long userId);

    ParticipantRequestDto addUserRequest(Long userId, Long eventId);

    ParticipantRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipantRequestDto> getUserEventParticipationRequest(Long userId, Long evenId);

    List<ParticipantRequestDto> confirmRequest(Long userId, Long eventId, ParticipantRequestUpdateDto requestUpdateDto);
}
