package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryServiceImpl;
import ru.practicum.model.category.categoryDto.CategoryDto;
import ru.practicum.model.compilation.CompilationService;
import ru.practicum.model.compilation.compilationDto.CompilationDto;
import ru.practicum.model.event.EventServiceImpl;
import ru.practicum.model.event.eventDto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final CategoryServiceImpl categoryService;
    private final EventServiceImpl eventService;
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilationByParam(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilationByParam(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getAllCategory(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoriesById(@PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        return eventService.getEventByParamPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEventByIdPublish(id, request);
    }

}
