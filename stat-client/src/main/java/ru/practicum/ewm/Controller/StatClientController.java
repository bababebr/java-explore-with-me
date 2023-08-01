package ru.practicum.ewm.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.Client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class StatClientController {

    private final StatsClient statClient;

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        return statClient.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> post(@Validated @RequestBody HitDto dto) {
        return statClient.post(dto);
    }
}
