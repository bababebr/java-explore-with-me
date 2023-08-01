package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    List<Hit> findAllByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime stop);

    @Query("SELECT h FROM Hit as h WHERE (h.timestamp BETWEEN ?1 AND ?2) AND h.uri IN ?3")
    List<Hit> findAllByTimestampAfterAndTimestampBeforeWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT h FROM Hit as h WHERE h.id IN (SELECT MIN(hh.id) FROM Hit as hh GROUP BY hh.ip, hh.uri) " +
            "AND (h.timestamp BETWEEN ?1 AND ?2) order by h.uri")
    List<Hit> getUniqueIpAndUriWithUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h FROM Hit as h WHERE h.id IN (SELECT MIN(hh.id) FROM Hit as hh GROUP BY hh.ip, hh.uri) " +
            "AND (h.timestamp BETWEEN ?1 AND ?2) order by h.uri")
    List<Hit> getUniqueIpAndUri(LocalDateTime start, LocalDateTime end);
}
