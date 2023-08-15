package ru.practicum.ewm.models.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.location.LocationDto;
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
    CategoryDto category;
    @NotNull
    Integer confirmedRequests = 0;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn = LocalDateTime.now();
    String description = "";
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Long id = 0L;
    @NotNull
    UserDtoShort initiator;
    @NotNull
    LocationDto location;
    @NotNull
    Boolean paid;
    Integer participantLimit = 0;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.S]")
    LocalDateTime publishedOn = LocalDateTime.now();
    Boolean requestModeration = true;
    EventStatus state = EventStatus.PENDING;
    @NotNull
    String title;
    Integer views = 0;

}
