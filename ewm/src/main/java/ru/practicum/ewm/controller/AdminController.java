package ru.practicum.ewm.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.user.UserDto;
import ru.practicum.ewm.service.interfaces.ICategoryService;
import ru.practicum.ewm.service.interfaces.ICompilationService;
import ru.practicum.ewm.service.interfaces.IEventService;
import ru.practicum.ewm.service.interfaces.IUserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    IUserService userService;
    ICategoryService categoryService;
    IEventService eventService;
    ICompilationService compilationService;

    @Autowired
    public AdminController(IUserService userService, ICategoryService categoryService,
                           IEventService eventService, ICompilationService compilationService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
    }

    /**
     * Admin: Users
     */
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

    /**
     * Admin:categories
     */
    @PostMapping("/categories")
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryService.add(categoryDto);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PatchMapping("/categories/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @Valid @RequestBody NewCategoryDto dto) {
        return categoryService.update(id, dto);
    }

    /**
     * Admin:Events
     */
    @GetMapping("/events")
    public List<EventFullDto> getUsersEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<EventStatus> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") @Min(0) int from,
                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return eventService.getUsersEvent(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody EventUpdateDto dto) {
        return eventService.updateEventByAdmin(eventId, dto);
    }

    /**
     * Admin: Compilations
     */

    @PostMapping("/compilations")
    CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto dto) {
        return compilationService.add(dto);
    }

}
