package ru.practicum.explore.comment.service;

import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    CommentDto findUserCommentById(Long userId, Long eventId);

    List<CommentDto> getCommentsPublic(Boolean sort, int from, int size);

    CommentDto getCommentPublicById(Long id);

    CommentDto findCommentById(Long id);
}
