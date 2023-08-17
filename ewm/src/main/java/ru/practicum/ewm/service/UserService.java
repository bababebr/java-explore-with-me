package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.IUserService;
import ru.practicum.ewm.models.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto add(UserDto userDto) {
        try {
            User user = repository.save(UserMapper.dtoToUser(userDto));
            return UserMapper.userToDto(user);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void delete(Long userId) {
        repository.findById(userId).orElseThrow(() -> new RuntimeException());
        repository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        System.out.println(ids);
        if (ids == null) {
            return repository.findAll(PageRequest.of(from, size)).stream().map(UserMapper::userToDto).collect(Collectors.toList());
        }
        return repository.findAllByIds(ids, PageRequest.of(from, size)).stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }
}
