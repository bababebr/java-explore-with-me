package ru.practicum.ewm.models.request.eventRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStatus;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    @NotNull
    Long id;
    @NotNull
    Long eventId;
    @NotNull
    Long userId;
    @NotNull
    EventStatus status;
}
