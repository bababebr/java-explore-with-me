package ru.practicum.ewm.models.participantRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.enums.ParticipantRequestStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantRequestUpdateDto {
    List<Long> requestIds;
    ParticipantRequestStatus status;
}
