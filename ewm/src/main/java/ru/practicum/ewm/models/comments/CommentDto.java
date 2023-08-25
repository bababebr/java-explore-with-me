package ru.practicum.ewm.models.comments;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
public class CommentDto {

    Long id;
    Long userId;
    Long eventId;
    String text;
    LocalDateTime created;
    LocalDateTime updated;
}
