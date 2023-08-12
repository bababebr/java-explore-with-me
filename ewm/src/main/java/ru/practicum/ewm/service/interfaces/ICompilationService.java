package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;

public interface ICompilationService {

    CompilationDto add(NewCompilationDto compilation);

    void delete(Long compilationId);

    CompilationDto update(CompilationDto compilationDto, Long compilationId);

}
