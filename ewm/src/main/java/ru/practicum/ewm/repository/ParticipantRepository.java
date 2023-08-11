package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.enums.ParticipantRequestStatus;
import ru.practicum.ewm.models.request.participantRequest.ParticipantRequest;

public interface ParticipantRepository extends JpaRepository<ParticipantRequest, Long> {

    Integer countAllByEventIdAndStatus(Long eventId, ParticipantRequestStatus status);

}
