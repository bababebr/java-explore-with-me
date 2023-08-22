package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.models.compilations.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    void deleteAllById(Long id);

    @Query("SELECT c FROM Compilation as c")
    List<Compilation> getAll(Pageable pageable);

}
