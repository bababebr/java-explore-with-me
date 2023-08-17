package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.models.compilations.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    void deleteAllByCompilationId(Long id);

    @Query("SELECT MAX(c.compilationId) FROM Compilation as c")
    Optional<Long> getNextCompilationId();

    @Query("SELECT DISTINCT(c.compilationId) FROM Compilation AS c")
    List<Long> getCompilationsIds(Pageable pageable);

    @Query("SELECT c FROM Compilation AS c WHERE c.compilationId = ?1")
    List<Compilation> getCompilationsById(Long id);

}
