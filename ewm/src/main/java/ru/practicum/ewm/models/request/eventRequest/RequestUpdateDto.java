package ru.practicum.ewm.models.request.eventRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.EventStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateDto {

    @NotEmpty
    List<Integer> usersIds;
    @NotNull
    EventStatus status;

}
