package ru.practicum.ewm.models.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.event.EventShortDto;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.models.user.UserDtoShort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
public class CommentDto {

    @NotNull
    Long userId;
    @NotNull
    Long eventId;
    @Length(max = 256)
    @NotBlank
    String text;
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
}
