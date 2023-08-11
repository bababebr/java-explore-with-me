package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.event.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    @Query("SELECT e FROM Event as e WHERE e.participantLimit < " +
            "(SELECT COUNT(pr.id) FROM ParticipantRequest as pr WHERE pr.eventId IN " +
            "(SELECT e.id FROM Event as e WHERE UPPER(e.annotation) like UPPER(concat('%',?1,'%'))))")
    List<Event> findEvents(String text, List<Integer> categories, boolean paid);

    /*
    SELECT *
FROM event AS e
WHERE (SELECT COUNT(pr.id)
       FROM participant_request as pr
       WHERE pr.event_id IN (SELECT e.id WHERE e.annotation like 'q')) < e.participant_limit
     */
}
