package ru.practicum.ewm.models.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.models.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Long id;
    List<EventShortDto> eventShortDto;
    @NotNull
    Boolean pinned;
    @NotBlank
    String title;

}
