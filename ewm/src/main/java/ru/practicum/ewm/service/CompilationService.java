package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
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
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        HashSet<Event> eventsSet = new HashSet<>();
        eventsSet.addAll(events);
        Compilation c = CompilationMapper.newCompilationToCompilation(eventsSet, compilationDto.getTitle(), compilationDto.getPinned());
        Compilation saved = repository.save(c);
        return CompilationMapper.compilationToDto(saved);
    }

    @Override
    public void delete(Long compilationId) {
        repository.deleteAllById(compilationId);
    }

    @Override
    public CompilationDto update(NewCompilationDto compilationDto, Long compilationId) {
        Compilation compilation = repository.findById(compilationId).orElseThrow(
                () -> new NoSuchElementException("Compilation not found."));
        updateDto(compilation, compilationDto);
        Compilation saved = repository.save(compilation);
        return CompilationMapper.compilationToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(int from, int size) {
        List<Compilation> compilations = repository.getAll(PageRequest.of(from, size));
        return compilations.stream().map(CompilationMapper::compilationToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Compilation not found."));
        return CompilationMapper.compilationToDto(compilation);
    }

    private void updateDto(Compilation compilation, NewCompilationDto newDto) {
        List<Event> events = eventRepository.findAllById(newDto.getEvents());
        compilation.setEvent(new HashSet<>(events));
        if (newDto.getPinned() != null) {
            compilation.setPinned(newDto.getPinned());
        }
        if (newDto.getTitle() != null && newDto.getTitle().length() <= 50) {
            compilation.setTitle(newDto.getTitle());
        }
    }

}
