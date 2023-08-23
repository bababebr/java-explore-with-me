package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.mapper.ParticipantRequestMapper;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.participantRequest.ParticipantRequestUpdateDto;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.IRequestService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestService implements IRequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public RequestService(EventRepository eventRepository, UserRepository userRepository,
                          ParticipantRepository participantRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipantRequestDto> getUserEventParticipationRequest(Long userId, Long evenId) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, Pageable.unpaged());
        List<Long> eventsIds = events.stream().map((e) -> e.getId()).collect(Collectors.toList());
        List<ParticipantRequest> requests = participantRepository.findAllByEventIdIn(eventsIds);
        return requests.stream().map(ParticipantRequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ParticipantRequestDto>> changeRequestStatus(Long userId, Long eventId, ParticipantRequestUpdateDto requestUpdateDto) {
        List<ParticipantRequest> requests = participantRepository.findAllByUserIdAndEventIdIn(eventId,
                requestUpdateDto.getRequestIds());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("Event not found"));

        ParticipantRequestStatus newStatus = requestUpdateDto.getStatus();
        List<ParticipantRequestDto> requestDtos = new ArrayList<>();

        for (ParticipantRequest request : requests) {
            int confirmedEventRequests = participantRepository.countAllByEventIdAndStatusIn(eventId,
                    List.of(ParticipantRequestStatus.CONFIRMED));
            if (confirmedEventRequests >= event.getParticipantLimit()) {
                throw new IllegalStateException("Participant limit has been reached.");
            }
            if (request.getStatus().equals(ParticipantRequestStatus.CONFIRMED) && requestUpdateDto.getStatus()
                    .equals(ParticipantRequestStatus.REJECTED)) {
                throw new IllegalStateException("Can't cancel confirmed request");
            }
            request.setStatus(newStatus);
            ParticipantRequest r = participantRepository.save(request);
            requestDtos.add(ParticipantRequestMapper.requestToDto(r));
        }

        HashMap<String, List<ParticipantRequestDto>> returnMap = new HashMap<>();
        switch (newStatus) {
            case CANCELED:
                returnMap.put("canceledRequests", requestDtos);
                break;
            case PENDING:
                returnMap.put("pendingRequests", requestDtos);
                break;
            case CONFIRMED:
                returnMap.put("confirmedRequests", requestDtos);
                break;
            case REJECTED:
                returnMap.put("rejectedRequests", requestDtos);
        }
        return returnMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantRequestDto> getUserRequest(Long userId) {
        List<ParticipantRequest> request = participantRepository.getUserRequests(userId);
        return request.stream().map(ParticipantRequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    public ParticipantRequestDto addUserRequest(Long userId, Long eventId) {
        return validateUserRequest(userId, eventId);
    }

    @Override
    public ParticipantRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipantRequest participantRequest = participantRepository.findAllByUserIdAndId(userId, requestId)
                .orElseThrow(() -> new NoSuchElementException("User request not found."));
        participantRequest.setStatus(ParticipantRequestStatus.CANCELED);

        return ParticipantRequestMapper.requestToDto(participantRepository.save(participantRequest));
    }

    private ParticipantRequestDto validateUserRequest(Long userId, Long eventId) {
        /**
         * Is Event and User exist
         */
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Event with ID=%s not found.", eventId)));
        userRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with ID=%s not found.", userId)));

        if (event.getState() != EventStatus.PUBLISHED) {
            throw new IllegalStateException(String.format("Event=%s not published, cannot create a request", event.getId()));
        }

        /**
         * Is request already exist
         */
        if (participantRepository.findByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new IllegalStateException(String.format("Request from User=%s for Event=%s already exist", userId, eventId));
        }
        /**
         * Is participant limit reached
         */
        Integer part = participantRepository.countAllByEventIdAndStatusIn(eventId,
                List.of(ParticipantRequestStatus.CONFIRMED));
        if (part >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new IllegalStateException("Participant limit has been reached.");
        }
        /**
         * Is User own Event
         */
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new IllegalStateException("Cannot create request for the own event.");
        }
        /**
         * Is event published
         */
        ParticipantRequestDto dto = ParticipantRequestDto
                .create(LocalDateTime.now(), 0L, eventId, userId, ParticipantRequestStatus.CONFIRMED);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            dto.setStatus(ParticipantRequestStatus.PENDING);
        }
        ParticipantRequest request;
        request = participantRepository.save(ParticipantRequestMapper.dtoToRequest(dto));
        return ParticipantRequestMapper.requestToDto(request);
    }
}
