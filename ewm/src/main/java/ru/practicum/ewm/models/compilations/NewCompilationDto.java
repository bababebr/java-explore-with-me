package ru.practicum.ewm.models.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor(staticName = "create")
public class NewCompilationDto {
    List<Long> event_ids;
    @NotNull
    Boolean pinned;
    @NotEmpty
    String title;
}
