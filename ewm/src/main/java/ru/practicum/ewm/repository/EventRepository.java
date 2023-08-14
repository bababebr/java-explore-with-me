package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    @Query("SELECT e FROM Event as e WHERE " +
            "(e.participantLimit < (SELECT COUNT(pr.id) FROM ParticipantRequest as pr WHERE pr.eventId = e.id AND pr.status <> 'REJECTED') or ?4 = false) " +
            "AND (e.paid = ?3 or ?3 is null) " +
            "AND (UPPER(e.annotation) like UPPER(concat('%',?1,'%')) OR ?1 is null) " +
            "AND (UPPER(e.description) like UPPER(concat('%',?1,'%')) OR ?1 is null) " +
            "AND (e.category.id IN ?2 OR ?2 is null) " +
            "AND ((e.eventDate BETWEEN ?5 AND ?6) OR length(?5) is null AND e.eventDate > current_timestamp) order by ?7")

    List<Event> getEvents(String text, List<Long> categories, boolean paid, boolean onlyAvailable, LocalDateTime start,
                          LocalDateTime end, String order, Pageable pageable);

    @Query("SELECT e FROM Event as e WHERE (e.initiator.id IN ?1 or 0 = ?1 ) AND (e.state in ?2 or ?2 is null) " +
            "AND (e.category.id IN ?3 or 0 = ?3) " +
            "AND (e.eventDate between ?4 AND ?5)")
    List<Event> getUserEvents(List<Long> usersId, List<EventStatus> states, List<Long> categories,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


    @Query("SELECT MAX(e.id) FROM Event as e")
    Optional<Long> getNextEventId();

}
