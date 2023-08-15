package ru.practicum.ewm.mapper;

import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.category.Category;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.event.NewEventDto;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.user.User;

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

    public static EventFullDto eventToFull(Event event, Integer confirmedRequests) {
        return EventFullDto.create(event.getAnnotation(),
                CategoryMapper.categoryToDto(event.getCategory()),
                confirmedRequests,
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.userToShort(event.getInitiator()),
                LocationMapper.locationToDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                LocalDateTime.now(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                0);
    }

    public static Event newEventToEvent(NewEventDto dto, Category category, User initiator, Location location) {
        return Event.create(0L,
                dto.getAnnotation(),
                category,
                LocalDateTime.now(),
                dto.getDescription(),
                dto.getEventDate(),
                initiator,
                location,
                dto.getPaid(),
                dto.getParticipantLimit(),
                dto.getRequestModeration(),
                EventStatus.PENDING,
                dto.getTitle());
    }
}
