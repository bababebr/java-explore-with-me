package ru.practicum.ewm.models.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor(staticName = "create")
public class NewCompilationDto {
    List<Long> events = new ArrayList<>();
    @NotNull
    Boolean pinned = false;
    @NotBlank
    @Length(max = 50)
    String title;
}
