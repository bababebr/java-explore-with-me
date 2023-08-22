package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventShortDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation newCompilationToCompilation(Set<Event> event, String title, boolean pinned) {
        return Compilation.create(0L, event, pinned, title);
    }

    public static Compilation dtoToCompilation(CompilationDto compilation, Set<Event> event) {
        return Compilation.create(0L, event, compilation.getPinned(), compilation.getTitle());
    }

    public static CompilationDto compilationToDto(Compilation c) {
        List<EventShortDto> shortDtoList = c.getEvent().stream().map(
                e -> EventMapper.eventToShort(e, 0)).collect(Collectors.toList());
        return CompilationDto.create(c.getId(), shortDtoList, c.getPinned(), c.getTitle());
    }
}
