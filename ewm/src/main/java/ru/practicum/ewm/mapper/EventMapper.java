package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventShortDto eventToShort(Event event) {
        return EventShortDto.create(event.getAnnotation(), CategoryMapper.categoryToDto(event.getCategory()),
                0,
                event.getEventDate(),
                event.getId(),
                UserMapper.userToShort(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                0);
    }

    public static EventFullDto eventToFull(Event event) {
        return EventFullDto.create(event.getAnnotation(),
                CategoryMapper.categoryToDto(event.getCategory()),
                0,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.userToShort(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                LocalDateTime.now(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0);
    }
}
