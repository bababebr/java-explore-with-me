package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.mapper.ParticipantRequestMapper;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.request.eventRequest.RequestDto;
import ru.practicum.ewm.models.request.eventRequest.RequestUpdateDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.IRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RequestService implements IRequestService {
    private final RequestRepository eventRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public RequestService(RequestRepository eventRequestRepository, EventRepository eventRepository,
                          UserRepository userRepository, ParticipantRepository participantRepository) {
        this.eventRequestRepository = eventRequestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
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
    public ParticipantRequestDto addUserRequest(Long userId, Long eventId) {
        return validateUserRequest(userId, eventId);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        return null;
    }

    private ParticipantRequestDto validateUserRequest(Long userId, Long eventId) {
        /**
         * Is request already exist
         */
        if (participantRepository.findByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new IllegalStateException(String.format("Request from User=%s for Event=%s already exist", userId, eventId));
        }
        /**
         * Is Event and User exist
         */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Event with ID=%s not found.", eventId)));
        userRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with ID=%s not found.", userId)));
        /**
         * Is participant limit reached
         */
        if (participantRepository.countAllByEventIdAndStatusIn(eventId,
                List.of(ParticipantRequestStatus.CONFIRMED,
                        ParticipantRequestStatus.PENDING)) >= event.getParticipantLimit()) {
            throw new IllegalStateException("Participant limit has been reached.");
        }

        /**
         * Is User own Event
         */
        if (event.getInitiator().getId() == userId) {
            throw new IllegalStateException("Cannot create request for the own event.");
        }
        /**
         * Is event published
         */
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalStateException("Event not in PUBLISHED state.");
        }
        ParticipantRequestDto dto = ParticipantRequestDto
                .create(LocalDateTime.now(), 0L, eventId, userId, ParticipantRequestStatus.CONFIRMED);
        ParticipantRequest request;
        if (event.getRequestModeration() != false) {
            dto.setStatus(ParticipantRequestStatus.PENDING);
        }
        request = participantRepository.save(ParticipantRequestMapper.dtoToRequest(dto));
        return ParticipantRequestMapper.requestToDto(request);
    }
}
