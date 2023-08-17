package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.event.Event;

public class CompilationMapper {
    public static Compilation newCompilationToCompilation(Event event, String title, boolean pinned, Long nextId) {
        return Compilation.create(0L, nextId, event, pinned, title);
    }

    public static Compilation dtoToCompilation(CompilationDto compilation, Event event) {
        return Compilation.create(0L, compilation.getId(), event, compilation.getPinned(), compilation.getTitle());
    }
}
