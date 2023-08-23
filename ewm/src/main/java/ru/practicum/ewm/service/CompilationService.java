package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.interfaces.ICompilationService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CompilationService implements ICompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Autowired
    public CompilationService(CompilationRepository repository, EventRepository eventRepository,
                              StatsClient statsClient) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
    }

    @Override
    public CompilationDto add(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        HashSet<Event> eventsSet = new HashSet<>();
        eventsSet.addAll(events);
        Compilation c = CompilationMapper.newCompilationToCompilation(eventsSet, compilationDto.getTitle(), compilationDto.getPinned());
        Compilation saved = repository.save(c);
        return CompilationMapper.compilationToDto(saved, new HashMap<>());
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
        return CompilationMapper.compilationToDto(saved, new HashMap<>());
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(int from, int size) {
        List<Compilation> compilations = repository.getAll(PageRequest.of(from, size));

        HashMap<Long, Integer> eventViewsMap = new HashMap<>();
        List<Event> events = new ArrayList<>();
        compilations.stream().map(c -> events.addAll(c.getEvent()));
        events.stream().forEach(e -> eventViewsMap.put(e.getId(), getEventViews(e)));

        List<CompilationDto> returnDtoList = compilations.stream().map(c -> CompilationMapper
                .compilationToDto(c, eventViewsMap)).collect(Collectors.toList());
        return returnDtoList;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Compilation not found."));
        HashMap<Long, Integer> eventViewsMap = new HashMap<>();
        compilation.getEvent().stream().map(e -> eventViewsMap.put(e.getId(), getEventViews(e)));
        return CompilationMapper.compilationToDto(compilation, eventViewsMap);
    }

    private void updateDto(Compilation compilation, NewCompilationDto newDto) {
        List<Event> events = eventRepository.findAllById(newDto.getEvents());
        compilation.setEvent(new HashSet<>(events));
        if (newDto.getPinned() != null) {
            compilation.setPinned(newDto.getPinned());
        }
        if (newDto.getTitle() == null) {
            return;
        }
        if (newDto.getTitle().length() > 50) {
            throw new ValidationException();
        }
        compilation.setTitle(newDto.getTitle());
    }

    private int getEventViews(Event event) {
        String uri = "/events/" + event.getId();
        ResponseEntity<Object> views = statsClient.getHitsCount(event.getPublishedDate(), LocalDateTime.now(),
                List.of(uri), true);
        try {
            return Integer.parseInt(views.getBody().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
