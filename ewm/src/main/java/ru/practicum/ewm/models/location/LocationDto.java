package ru.practicum.ewm.models.location;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
public class LocationDto {
    Long id;
    @NotNull
    Long eventId;
    @NotNull
    @Size(min = -90, max = 90)
    Double lat;
    @NotNull
    @Size(min = -180, max = 180)
    Double lon;
}
