package ru.practicum.ewm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.interfaces.IUserService;
import ru.practicum.ewm.models.user.UserDto;

import javax.validation.Valid;

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

}
