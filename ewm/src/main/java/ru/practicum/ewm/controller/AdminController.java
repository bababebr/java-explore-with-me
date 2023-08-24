package ru.practicum.ewm.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.models.category.CategoryDto;
import ru.practicum.ewm.models.category.NewCategoryDto;
import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.compilations.CompilationDto;
import ru.practicum.ewm.models.compilations.NewCompilationDto;
import ru.practicum.ewm.models.event.EventFullDto;
import ru.practicum.ewm.models.event.EventUpdateDto;
import ru.practicum.ewm.models.user.UserDto;
import ru.practicum.ewm.service.interfaces.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    IUserService userService;
    ICategoryService categoryService;
    IEventService eventService;
    ICompilationService compilationService;
    ICommentService commentService;

    @Autowired
    public AdminController(IUserService userService, ICategoryService categoryService,
                           IEventService eventService, ICompilationService compilationService,
                           ICommentService commentService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.compilationService = compilationService;
        this.commentService = commentService;
    }

    /**
     * Admin: Users
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto user) {
        return userService.add(user);
    }

    @GetMapping("/users")
    public List<UserDto> get(@RequestParam(name = "ids", required = false) List<Long> usersId,
                             @RequestParam(defaultValue = "0") @Min(0) int from,
                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return userService.getUsers(usersId, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

    /**
     * Admin:categories
     */
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryService.add(categoryDto);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
    public List<EventFullDto> getUsersEvents(@RequestParam(defaultValue = "0") List<Long> users,
                                             @RequestParam(required = false) List<EventStatus> states,
                                             @RequestParam(defaultValue = "0") List<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") @Min(0) int from,
                                             @RequestParam(defaultValue = "10") @Min(1) int size) {
        return eventService.getUsersEvent(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventUpdateDto dto) {
        return eventService.updateEventByAdmin(eventId, dto);
    }

    /**
     * Admin: Compilations
     */

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto dto) {
        return compilationService.add(dto);
    }

    @PatchMapping("/compilations/{id}")
    CompilationDto updateCompilation(@RequestBody NewCompilationDto dto,
                                     @PathVariable Long id) {
        return compilationService.update(dto, id);
    }

    @DeleteMapping("/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long id) {
        compilationService.delete(id);
    }

    /**
     * Comments
     */
    @GetMapping("/comments/event/{eventId}")
    public List<CommentDto> getEventComments(@PathVariable Long eventId) {
        return commentService.getEventComments(eventId);
    }
    @GetMapping("/comments/user/{userId}")
    public List<CommentDto> getUserComments(@PathVariable Long userId) {
        return commentService.getUserComments(userId);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId) {
        return commentService.getById(commentId);
    }
}
