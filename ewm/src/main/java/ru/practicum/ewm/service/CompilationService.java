package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.models.compilations.Compilation;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.interfaces.ICompilationService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        return null;
    }

    @Override
    public void delete(Long compilationId) {

    }

    @Override
    public CompilationDto update(CompilationDto compilationDto, Long compilationId) {
        return null;
    }

    private CompilationDto saveCompilations(NewCompilationDto newCompilationDto) {
        List<Compilation> compilationList = new ArrayList<>();
        for (Long eventId : newCompilationDto.getEvent_ids()) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
            Compilation compilation = Compilation.create();
            compilation.setEvent(event);
            compilation.setPinned(newCompilationDto.getPinned());
            compilation.setTitle(newCompilationDto.getTitle());
            compilationList.add(repository.save(compilation));
        }
        CompilationDto compilationDto = CompilationDto.create()
    }
}
