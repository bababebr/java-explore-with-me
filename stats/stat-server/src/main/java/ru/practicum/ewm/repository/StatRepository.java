package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h FROM Hit as h WHERE((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) " +
            "AND (h.uri in ?3 OR ?3 is null)" +
            "AND ((h.id in (SELECT MIN(subHit.id) FROM Hit as subHit GROUP BY subHit.uri, subHit.ip)) OR ?4 = false )")
    List<Hit> findUniqueHits(LocalDateTime start, LocalDateTime stop, List<String> uris, boolean unique);

    @Query("SELECT COUNT(h) FROM  Hit as h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) " +
            "AND (h.uri in ?3 or length(?3) is null)")
    Integer countHits(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT DISTINCT COUNT(DISTINCT h.ip) FROM  Hit as h WHERE ((h.timestamp BETWEEN ?1 AND ?2) or length(?1) is null) " +
            "AND (h.uri in ?3 or length(?3) is null)")
    Integer countUniqueHits(LocalDateTime start, LocalDateTime end, List<String> uris);
}
