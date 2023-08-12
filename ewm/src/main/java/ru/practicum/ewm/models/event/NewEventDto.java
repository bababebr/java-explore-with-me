package ru.practicum.ewm.models.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class NewEventDto {
    @NotNull
    @NotEmpty
    @NotBlank
    String annotation;
    Long categoryId;
    @NotNull
    @NotEmpty
    @NotBlank
    String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    Boolean paid = false;
    Integer participantLimit = 0;
    Boolean requestModeration = true;
    @NotNull
    String title;
}
