package ru.practicum.explore.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.service.CommentService;

import javax.validation.constraints.Positive;
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
    public List<CommentDto> getCommentsPublic(@RequestParam(defaultValue = "id,desc") String[] sort,
                                              @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(value = "size", defaultValue = "10") @Positive int size) {

        log.info("Получение опубликованных комментариев.");
        return commentService.getCommentsPublic(sort, from, size);

    }

    @GetMapping("comments/{commentId}")
    public CommentDto getCommentPublicById(@PathVariable(name = "commentId") Long id) {

        log.info("Получение комментария с id = " + id);
        return commentService.getCommentPublicById(id);

    }
}
