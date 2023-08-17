package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h FROM Hit AS h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null)")
    List<Hit> findAllByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime stop);

    @Query("SELECT h FROM Hit as h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) AND h.uri IN ?3")
    List<Hit> findAllByTimestampAfterAndTimestampBeforeWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT h FROM Hit as h WHERE h.id IN (SELECT MIN(hh.id) FROM Hit as hh GROUP BY hh.ip, hh.uri) " +
            "AND ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) order by h.uri")
    List<Hit> getUniqueIpAndUriWithUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h FROM Hit as h WHERE h.id IN (SELECT MIN(hh.id) FROM Hit as hh GROUP BY hh.ip, hh.uri) " +
            "AND (((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null)) order by h.uri")
    List<Hit> getUniqueIpAndUri(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(h) FROM  Hit as h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) " +
            "AND (h.uri in ?3 or length(?3) is null)")
    Integer countHits(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT DISTINCT COUNT(DISTINCT h.ip) FROM  Hit as h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) " +
            "AND (h.uri in ?3 or length(?3) is null)")
    Integer countUniqueHits(LocalDateTime start, LocalDateTime end, List<String> uris);
}
