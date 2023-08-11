package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventShortDto;

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

}
