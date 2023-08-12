package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    @Query("SELECT e FROM Event as e  WHERE (e.participantLimit < " +
            "(SELECT COUNT(pr.id) FROM ParticipantRequest as pr WHERE pr.eventId IN " +
            "(SELECT e.id FROM Event as e WHERE UPPER(e.annotation) like UPPER(concat('%',?1,'%')) OR " +
            "UPPER(e.description) like UPPER(concat('%',?1,'%'))))) " +
            "AND e.category in ?2" +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5" +
            " ORDER BY ?6")
    List<Event> findEventsIsAvailableWithRange(String text, List<Integer> categories, boolean paid, LocalDateTime start,
                                               LocalDateTime end, String order);

    @Query("SELECT e FROM Event as e  WHERE (e.participantLimit < " +
            "(SELECT COUNT(pr.id) FROM ParticipantRequest as pr WHERE pr.eventId IN " +
            "(SELECT e.id FROM Event as e WHERE UPPER(e.annotation) like UPPER(concat('%',?1,'%')) OR " +
            "UPPER(e.description) like UPPER(concat('%',?1,'%'))))) " +
            "AND e.category in ?2" +
            "AND e.paid = ?3 " +
            "AND e.eventDate > CURRENT_TIME" +
            " ORDER BY ?4")
    List<Event> findEventsIsAvailableWithoutRange(String text, List<Integer> categories, boolean paid, String order);

    @Query("SELECT e FROM Event as e WHERE UPPER(e.annotation) like UPPER(concat('%',?1,'%')) OR " +
            "UPPER(e.description) like UPPER(concat('%',?1,'%')) " +
            "AND e.category in ?2" +
            "AND e.paid = ?3 " +
            "AND e.eventDate > CURRENT_TIME" +
            " ORDER BY ?4")
    List<Event> findEventsWithoutRange(String text, List<Integer> categories, boolean paid, String order);

    @Query("SELECT e FROM Event as e WHERE UPPER(e.annotation) like UPPER(concat('%',?1,'%')) OR " +
            "UPPER(e.description) like UPPER(concat('%',?1,'%')) " +
            "AND e.category in ?2" +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5" +
            " ORDER BY ?6")
    List<Event> findEventsWithRange(String text, List<Integer> categories, boolean paid, LocalDateTime start,
                                    LocalDateTime end, String order);

    @Query(value = "SELECT e FROM event as e WHERE :allUsers = TRUE THEN ")
    List<Event> getAllUserEvents(@Param("allUsers") boolean allUsers);

    @Query("SELECT MAX(e.id) FROM Event as e")
    Optional<Long> getNextEventId();
    /*
    SELECT *
FROM event AS e
WHERE (SELECT COUNT(pr.id)
       FROM participant_request as pr
       WHERE pr.event_id IN (SELECT e.id WHERE e.annotation like 'q')) < e.participant_limit
     */
}
