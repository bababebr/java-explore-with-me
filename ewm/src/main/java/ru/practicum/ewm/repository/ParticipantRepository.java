package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.models.request.eventRequest.Request;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantRequest, Long> {

    Integer countAllByEventIdAndStatus(Long eventId, ParticipantRequestStatus status);

    Optional<Request> findByUserIdAndEventId(Long userId, Long eventId);

    Integer countAllByEventIdAndStatusIn(Long eventId, List<ParticipantRequestStatus> statuses);

}
