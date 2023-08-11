package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.request.eventRequest.RequestDto;
import ru.practicum.ewm.models.request.eventRequest.RequestUpdateDto;

import java.util.List;

public interface IRequestService {
    RequestDto get(Long userId, Long eventId);
    RequestDto updateRequest(Long userId, Long eventId, RequestUpdateDto dto);

    List<RequestDto> getUserRequest(Long userId);

    RequestDto addUserRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
