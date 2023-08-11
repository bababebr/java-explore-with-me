package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.models.request.RequestDto;
import ru.practicum.ewm.models.request.RequestUpdateDto;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.service.interfaces.IRequestService;

import java.util.List;

@Service
public class RequestService implements IRequestService {
    private final RequestRepository repository;

    @Autowired
    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public RequestDto get(Long userId, Long eventId) {
        return null;
    }

    @Override
    public RequestDto updateRequest(Long userId, Long eventId, RequestUpdateDto dto) {
        return null;
    }

    @Override
    public List<RequestDto> getUserRequest(Long userId) {
        return null;
    }

    @Override
    public RequestDto addUserRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        return null;
    }
}
