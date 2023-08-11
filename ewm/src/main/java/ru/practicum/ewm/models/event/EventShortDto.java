package ru.practicum.ewm.models.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.user.UserDtoShort;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    @NotNull
    String annotation;
    @NotNull
    CategoryDto categoryDto;
    Integer confirmedRequest = 0;
    LocalDateTime eventDate;
    Long id = 0L;
    @NotNull
    UserDtoShort initiator;
    @NotNull
    Boolean paid;
    String title;
    Integer views = 0;
}
