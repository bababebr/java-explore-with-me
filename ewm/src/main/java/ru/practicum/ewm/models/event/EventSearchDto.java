package ru.practicum.ewm.models.event;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.enums.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSearchDto {
    String text;
    List<Long> categories;
    Boolean paid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    LocalDateTime end;
    Boolean onlyAvailable;
    Sort sort;

}
