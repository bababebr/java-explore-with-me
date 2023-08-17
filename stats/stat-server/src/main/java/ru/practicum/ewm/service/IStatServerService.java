package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitDtoShort;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatServerService {

    HitDto add(HitDto dto);

    List<HitDtoShort> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    Integer getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
