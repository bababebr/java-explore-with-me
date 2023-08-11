package ru.practicum.ewm.models.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.models.location.Location;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotNull
    String annotation;
    @NotNull
    Integer categoryId;
    String description = "";
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    Location location;
    Boolean paid = false;
    Integer participantLimit = 0;
    Boolean requestModeration = true;
    @NotNull
    String title;
}
