package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.models.compilations.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {


    List<Compilation> findAllByTitleAndPinned(String title, Boolean pinned);

    @Query("SELECT MAX(c.compilationId) FROM Compilation as c")
    Long getNextCompilationId();
}
