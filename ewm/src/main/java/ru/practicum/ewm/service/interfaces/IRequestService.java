package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.participantRequest.ParticipantRequestUpdateDto;

import java.util.List;
import java.util.Map;

public interface IRequestService {
    /**
     * Participants requests
     */
    List<ParticipantRequestDto> getUserRequest(Long userId);

    ParticipantRequestDto addUserRequest(Long userId, Long eventId);

    ParticipantRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipantRequestDto> getUserEventParticipationRequest(Long userId, Long evenId);

    Map<String, List<ParticipantRequestDto>> changeRequestStatus(Long userId, Long eventId, ParticipantRequestUpdateDto requestUpdateDto);
}
