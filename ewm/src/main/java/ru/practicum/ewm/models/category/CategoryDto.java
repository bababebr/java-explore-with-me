package ru.practicum.ewm.models.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
@RequiredArgsConstructor(staticName = "create")
public class CategoryDto {
    Long id;
    @NotEmpty
    @Size(min = 1, max = 50)
    String name;
}
