package ru.practicum.explore.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.UpdateAdminCommentDto;
import ru.practicum.explore.comment.service.CommentService;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompilationRequestDto;
import ru.practicum.explore.compilation.service.CompilationService;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.user.dto.NewUserRequestDto;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.service.UserService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final CommentService commentService;

    @GetMapping(value = "/users")
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(value = "from", required = false, defaultValue = "0")
                                  @PositiveOrZero int from,
                                  @RequestParam(value = "size", required = false, defaultValue = "10")
                                  @PositiveOrZero int size) {
        log.info("Получение информации о пользователях.");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody NewUserRequestDto newUserRequestDto) {
        log.info("Добавление нового пользователя с email: " + newUserRequestDto.getEmail());
        return userService.createUser(newUserRequestDto);
    }

    @DeleteMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("userId") Long id) {
        log.info("Удаление пользователя с id: " + id);
        userService.deleteUserById(id);
    }

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Добавление новой категории.");
        return categoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping(value = "/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Удаление категории с id: " + catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping(value = "/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto,
                                      @PathVariable("catId") Long catId) {
        log.info("Обновление категории с id: " + catId);
        return categoryService.updateCategory(categoryDto, catId);
    }

    @GetMapping(value = "/events")
    public List<EventFullDto> getFullEvents(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<StateEvent> states,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(value = "from", defaultValue = "0")
                                            @PositiveOrZero int from,
                                            @RequestParam(value = "size", defaultValue = "10")
                                            @PositiveOrZero int size) {
        log.info("Получение списка событий.");
        return eventService.getFullEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(value = "/events/{eventId}")
    public EventFullDto updateEventByAdmin(@RequestBody UpdateEventAdminRequestDto updateEventDto,
                                           @PathVariable("eventId") Long eventId) {
        log.info("Редактирование данных события и его статуса.");

        return eventService.updateEventByAdmin(updateEventDto, eventId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки.");

        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("Удаление подборки.");

        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long compId,
                                            @RequestBody UpdateCompilationRequestDto updateCompilationDto) {
        log.info("Обновление подборки событий с id: " + compId);

        return compilationService.updateCompilation(compId, updateCompilationDto);
    }

    @PatchMapping(value = "/comments/{commentId}")
    public CommentDto updateCommentByAdmin(@RequestBody UpdateAdminCommentDto updateAdminComment,
                                           @PathVariable("commentId") Long commentId) {
        log.info("Модерация комментария и его статуса.");

        return commentService.updateCommentByAdmin(updateAdminComment, commentId);
    }
}

