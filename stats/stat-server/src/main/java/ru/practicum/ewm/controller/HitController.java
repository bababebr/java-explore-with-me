package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitDtoShort;
import ru.practicum.ewm.service.IStatServerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
public class HitController {

    private final IStatServerService service;

    @Autowired
    public HitController(IStatServerService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto post(@RequestBody HitDto dto) {
        return service.add(dto);
    }

    @GetMapping("/stats")
    public List<HitDtoShort> get(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") boolean unique) {
        return service.get(start, end, uris, unique);
    }

    @GetMapping("/hits")
    public Integer getHits(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                           @RequestParam(required = false) List<String> uris,
                           @RequestParam(defaultValue = "false") boolean unique) {
        return service.getHits(start, end, uris, unique);
    }
}
