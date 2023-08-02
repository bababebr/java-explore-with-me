package ru.practicum.ewm.model;

import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitDtoShort;

public class HitMapper {

    public static HitDto hitToDto(Hit hit) {
        return HitDto.create(hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp());
    }

    public static Hit dtoToHit(HitDto dto) {
        return Hit.create(0L, dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimestamp());
    }

    public static HitDtoShort hitToDtoShort(Hit hit) {
        return HitDtoShort.create(hit.getApp(),
                hit.getUri(),
                0L);
    }

}
