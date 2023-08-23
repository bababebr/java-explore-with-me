package ru.practicum.ewm.models.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.models.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Long id;
    List<EventShortDto> events = new ArrayList<>();
    @NotNull
    Boolean pinned;
    @NotBlank
    @Length(max = 50)
    String title;

}
