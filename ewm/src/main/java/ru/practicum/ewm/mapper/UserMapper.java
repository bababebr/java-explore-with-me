package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.models.user.UserDto;

public class UserMapper {

    public static User dtoToUser(UserDto userDto) {
        return User.create(0L, userDto.getEmail(), userDto.getName());
    }

    public static UserDto userToDto(User user) {
        return UserDto.create(user.getId(), user.getEmail(), user.getName());
    }

}
