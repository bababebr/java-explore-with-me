package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.user.UserDto;

import java.util.List;

public interface IUserService {

    UserDto add(UserDto userDto);

    void delete(Long userId);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
