package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;

public class ParticipantRequestMapper {

    public static ParticipantRequestDto requestToDto(ParticipantRequest request) {
        return ParticipantRequestDto.create(request.getCreated(), request.getId(),
                request.getEventId(), request.getUserId(), request.getStatus());
    }

    public static ParticipantRequest dtoToRequest(ParticipantRequestDto dto) {
        return ParticipantRequest.create(0L, dto.getCreated(),dto.getRequester(), dto.getEvent(), dto.getStatus());
    }

}
