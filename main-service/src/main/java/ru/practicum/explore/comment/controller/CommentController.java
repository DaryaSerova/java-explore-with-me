package ru.practicum.explore.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.service.CommentService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("comments")
    public List<CommentDto> getCommentsPublic(@RequestParam(required = false) Boolean sort,
                                              @RequestParam(required = false, value = "from", defaultValue = "0")
                                              @PositiveOrZero int from,
                                              @RequestParam(required = false, value = "size", defaultValue = "10")
                                              @PositiveOrZero int size) {

        log.info("Получение опубликованных комментариев.");
        return commentService.getCommentsPublic(sort, from, size);

    }

    @GetMapping("comments/{id}")
    public CommentDto getCommentPublicById(@PathVariable(name = "id") Long id) {

        log.info("Получение комментария с id = " + id);
        return commentService.getCommentPublicById(id);

    }
}
