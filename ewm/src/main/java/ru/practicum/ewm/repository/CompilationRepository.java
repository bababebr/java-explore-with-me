package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.models.compilations.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {


    List<Compilation> findAllByTitleAndPinned(String title, Boolean pinned);

    Void deleteAllByCompilationId(Long id);

    @Query("SELECT MAX(c.compilationId) FROM Compilation as c")
    Long getNextCompilationId();

    @Query("SELECT DISTINCT(c.compilationId) FROM Compilation AS c")
    List<Long> getCompilationsIds();

    @Query("SELECT c FROM Compilation AS c WHERE c.compilationId = ?1")
    List<Compilation> getCompilationsById(Long id);

}
