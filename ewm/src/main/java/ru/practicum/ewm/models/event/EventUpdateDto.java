package ru.practicum.ewm.models.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.location.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {
    String annotation;
    Integer categoryId;
    String description;
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventStatus state;
    String title;
}
