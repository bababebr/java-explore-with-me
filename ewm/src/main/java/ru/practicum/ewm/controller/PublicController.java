package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
public class PublicController {

    private final EventService eventService;

    private final CompilationService compilationService;

    private final CategoryService categoryService;

    @Autowired
    public PublicController(EventService eventService, CompilationService compilationService,
                            CategoryService categoryService) {
        this.eventService = eventService;
        this.compilationService = compilationService;
        this.categoryService = categoryService;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(defaultValue = "0") String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "VIEWS") Sort sort,
                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "10") @Min(1) int size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "0") @Min(0) int from,
                                                @RequestParam(defaultValue = "10") @Min(1) int size) {
        return compilationService.getCompilations(from, size);
    }

    @GetMapping("/compilations/{id}")
    public CompilationDto getCompilation(@PathVariable Long id) {
        return compilationService.getCompilation(id);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(1) int size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.get(id);
    }

}
