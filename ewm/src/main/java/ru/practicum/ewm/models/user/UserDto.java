package ru.practicum.ewm.models.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotNull
    @Email
    @Length(min = 6, max = 254)
    String email;
    @NotBlank
    @Length(min = 2, max = 250)
    String name;
}
