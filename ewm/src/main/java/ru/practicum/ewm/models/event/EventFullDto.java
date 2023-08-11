package ru.practicum.ewm.models.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.location.Location;
import ru.practicum.ewm.models.user.UserDtoShort;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    @NotNull
    String annotation;
    @NotNull
    CategoryDto categoryDto;
    Integer confirmedRequest = 0;
    LocalDateTime createdOn = LocalDateTime.now();
    String description = "";
    @NotNull
    LocalDateTime eventDate;
    Long id = 0L;
    @NotNull
    UserDtoShort initiator;
    @NotNull
    Location location;
    @NotNull
    Boolean paid;
    Integer participantLimit = 0;
    LocalDateTime publishedOn = LocalDateTime.now();
    Boolean requestModeration = true;
    State state = State.PENDING;
    @NotNull
    String title;
    Integer views = 0;

}
