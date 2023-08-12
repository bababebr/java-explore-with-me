package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.enums.Sort;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.service.EventService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicController {

    private final EventService eventService;

    @Autowired
    public PublicController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) boolean paid,
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(required = false) boolean onlyAvailable,
                                         @RequestParam(required = false) Sort sort,
                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "10") @Min(1) int size) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

}
