package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.models.compilations.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {


    List<Compilation> findAllByTitleAndPinned(String title, Boolean pinned);

}
