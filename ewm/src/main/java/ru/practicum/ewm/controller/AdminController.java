package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.models.user.UserDto;
import ru.practicum.ewm.service.interfaces.IUserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    IUserService userService;

    @Autowired
    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserDto add(@Valid @RequestBody UserDto user) {
        return userService.add(user);
    }

    @GetMapping("/users")
    public List<UserDto> get(List<Long> usersId,
                             @RequestParam(defaultValue = "0") @Min(0) int from,
                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return userService.getUsers(usersId, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
