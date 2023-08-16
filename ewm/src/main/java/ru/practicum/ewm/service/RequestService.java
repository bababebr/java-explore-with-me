package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.mapper.ParticipantRequestMapper;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.request.eventRequest.RequestDto;
import ru.practicum.ewm.models.request.eventRequest.RequestUpdateDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestDto;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequestUpdateDto;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.IRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public RequestDto getEventRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public RequestDto updateEventRequest(Long userId, Long eventId, RequestUpdateDto dto) {
        return null;
    }

    @Override
    public List<ParticipantRequestDto> getUserEventParticipationRequest(Long userId, Long evenId) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, Pageable.unpaged());
        List<Long> eventsIds = events.stream().map((e) -> e.getId()).collect(Collectors.toList());
        List<ParticipantRequest> requests = participantRepository.findAllByEventIdIn(eventsIds);
        return requests.stream().map(ParticipantRequestMapper::requestToDto).collect(Collectors.toList());
    }

    @Override
    public ParticipantRequestDto updateRequest(Long userId, Long eventId, ParticipantRequestDto dto) {
        ParticipantRequest request = participantRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        request.setStatus(dto.getStatus());
        return ParticipantRequestMapper.requestToDto(participantRepository.save(request));
    }

    @Override
    public List<ParticipantRequestDto> confirmRequest(Long userId, Long eventId, ParticipantRequestUpdateDto requestUpdateDto) {
        List<ParticipantRequest> requests = participantRepository.findAllByUserIdAndEventIdAndIdIn(eventId,
                requestUpdateDto.getRequestIds());
        System.out.println(requests.size());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("Event not found"));

        ParticipantRequestStatus newStatus = requestUpdateDto.getStatus();
        List<ParticipantRequestDto> requestDtos = new ArrayList<>();

        for (ParticipantRequest request : requests) {
            int confirmedEventRequests = participantRepository.countAllByEventIdAndStatusIn(eventId,
                    List.of(ParticipantRequestStatus.CONFIRMED));
            System.out.println("confirmed " + confirmedEventRequests + " limit " + event.getParticipantLimit());
            if(confirmedEventRequests >= event.getParticipantLimit()) {
                throw new IllegalStateException("Participant limit has been reached.");
            }
            request.setStatus(newStatus);
            ParticipantRequest r = participantRepository.save(request);
            requestDtos.add(ParticipantRequestMapper.requestToDto(r));
        }
        return requestDtos;
    }

    @Override
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
        if (event.getInitiator().getId() == userId) {
            throw new IllegalStateException("Cannot create request for the own event.");
        }
        /**
         * Is event published
         */
        ParticipantRequestDto dto = ParticipantRequestDto
                .create(LocalDateTime.now(), 0L, eventId, userId, ParticipantRequestStatus.CONFIRMED);
        if (event.getRequestModeration() == true && event.getParticipantLimit() != 0) {
            dto.setStatus(ParticipantRequestStatus.PENDING);
        }
        ParticipantRequest request;
        request = participantRepository.save(ParticipantRequestMapper.dtoToRequest(dto));
        return ParticipantRequestMapper.requestToDto(request);
    }
}
