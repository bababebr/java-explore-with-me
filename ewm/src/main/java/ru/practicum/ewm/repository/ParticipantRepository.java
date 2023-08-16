package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.models.request.eventRequest.Request;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<ParticipantRequest, Long> {

    Optional<ParticipantRequest> findByUserIdAndEventId(Long userId, Long eventId);


    Integer countAllByEventIdAndStatus(Long eventId, ParticipantRequestStatus status);

    @Query("SELECT COUNT(p.id) FROM ParticipantRequest as p WHERE p.eventId = ?1 AND p.status in ?2")
    Integer countAllByEventIdAndStatusIn(Long eventId, List<ParticipantRequestStatus> statuses);

    @Query("SELECT p FROM ParticipantRequest AS p JOIN Event as e on e.id = p.eventId WHERE p.userId = ?1 AND e.initiator.id <> ?1")
    List<ParticipantRequest> getUserRequests(Long userId);

    Optional<ParticipantRequest> findAllByUserIdAndId(Long userId, Long requestId);

    @Query("SELECT r FROM ParticipantRequest as r WHERE (r.id IN ?2) AND (r.eventId =?1)")
    List<ParticipantRequest> findAllByUserIdAndEventIdAndIdIn(Long eventId, List<Long> ids);

    List<ParticipantRequest> findAllByEventIdIn(List<Long> eventId);
}
