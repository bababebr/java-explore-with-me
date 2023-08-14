package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.event.Event;

import java.util.List;

public class CompilationMapper {

    public static Compilation newCompilationToCompilation(Event event, String title, boolean pinned  ,Long nextId) {
        return Compilation.create(0L, nextId, event, pinned, title);
    }
}
