package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryServiceImpl;
import ru.practicum.model.category.categoryDto.CategoryDto;
import ru.practicum.model.category.categoryDto.NewCategoryDto;
import ru.practicum.model.compilation.CompilationService;
import ru.practicum.model.compilation.compilationDto.CompilationDto;
import ru.practicum.model.compilation.compilationDto.NewCompilationDto;
import ru.practicum.model.compilation.compilationDto.UpdateCompilationDto;
import ru.practicum.model.event.EventServiceImpl;
import ru.practicum.model.event.eventDto.EventFullDto;
import ru.practicum.model.event.eventDto.UpdateEventAdminRequest;
import ru.practicum.model.user.UserServiceImpl;
import ru.practicum.model.user.userDto.NewUserRequest;
import ru.practicum.model.user.userDto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;
    private final EventServiceImpl eventService;
    private final CompilationService compilationService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(catId, newCategoryDto.getName());
    }

    @GetMapping("/events")
    public List<EventFullDto> getAllEvents(
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Long> categories
    ) {
        return eventService.getEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest adminEvent) {
        return eventService.updateEventByAdmin(adminEvent, eventId);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.createUser(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody UpdateCompilationDto newCompilationDto) {
        return compilationService.updateCompilation(compId, newCompilationDto);
    }


}
