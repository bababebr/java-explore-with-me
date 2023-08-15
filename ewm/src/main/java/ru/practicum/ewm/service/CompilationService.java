package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.interfaces.ICompilationService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CompilationService implements ICompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationService(CompilationRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto add(NewCompilationDto compilationDto) {
        Long nextCompilationId = repository.getNextCompilationId().orElse(1L);
        CompilationDto returnDto = CompilationDto.create();
        if (compilationDto.getEvents() == null || compilationDto.getEvents().isEmpty()) {
            repository.save(CompilationMapper.newCompilationToCompilation(null, compilationDto.getTitle(),
                    compilationDto.getPinned(), nextCompilationId));
        } else {
            for (Long eventId : compilationDto.getEvents()) {
                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NoSuchElementException("Event not found"));
                returnDto.getEvents().add(EventMapper.eventToShort(event));
            }
        }
        returnDto.setPinned(compilationDto.getPinned());
        returnDto.setTitle(compilationDto.getTitle());
        returnDto.setId(nextCompilationId);
        return returnDto;
    }

    @Override
    public void delete(Long compilationId) {
        repository.deleteAllByCompilationId(compilationId);
    }

    @Override
    public CompilationDto update(NewCompilationDto compilationDto, Long compilationId) {
        repository.deleteAllByCompilationId(compilationId);
        return add(compilationDto);
    }

    public List<CompilationDto> getCompilations() {
        List<Long> ids = repository.getCompilationsIds();
        List<CompilationDto> compilationDtos = new ArrayList<>();
        if (ids.isEmpty()) {
            return compilationDtos;
        }

        for (Long id : ids) {
            List<Compilation> compilation = repository.getCompilationsById(id);
            List<EventShortDto> eventShortDtos = new ArrayList<>();
            if (compilation.isEmpty()) {
                continue;
            }
            eventShortDtos.addAll(compilation.stream().map(c -> EventMapper.eventToShort(c.getEvent()))
                    .collect(Collectors.toList()));
            CompilationDto compilationDto = CompilationDto.create(id, eventShortDtos, eventShortDtos.get(0).getPaid(),
                    eventShortDtos.get(0).getTitle());
            compilationDtos.add(compilationDto);
        }
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilation(Long id) {
        List<Compilation> compilations = repository.getCompilationsById(id);
        CompilationDto compilationDto = CompilationDto.create();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        if (compilations.isEmpty()) {
            return compilationDto;
        } else {
            for (Compilation c : compilations) {
                eventShortDtos.add(EventMapper.eventToShort(c.getEvent()));
            }
            compilationDto.setId(compilations.get(0).getCompilationId());
            compilationDto.setEvents(eventShortDtos);
            compilationDto.setTitle(compilations.get(0).getTitle());
            compilationDto.setPinned(compilations.get(0).getPinned());
            return compilationDto;
        }
    }
}
