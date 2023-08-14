package ru.practicum.ewm.models.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.location.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateDto {
    @Length(min = 20, max = 2000)
    String annotation;
    Long categoryId;
    @Length(min = 20, max = 7000)
    String description;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.S]")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    EventStatus state;
    @Length(min = 3, max = 120)
    String title;
}
