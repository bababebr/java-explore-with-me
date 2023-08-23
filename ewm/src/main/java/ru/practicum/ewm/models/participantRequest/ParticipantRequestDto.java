package ru.practicum.ewm.models.participantRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.ParticipantRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    LocalDateTime created;
    Long id;
    Long event;
    Long requester;
    ParticipantRequestStatus status;
}
