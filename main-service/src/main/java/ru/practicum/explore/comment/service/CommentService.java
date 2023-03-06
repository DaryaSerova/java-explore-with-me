package ru.practicum.explore.comment.service;

import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentDto;
import ru.practicum.explore.comment.dto.UpdateAdminCommentDto;
import ru.practicum.explore.comment.dto.UpdateUserCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    List<CommentDto> getCommentsPublic(String[] sort, int from, int size);

    CommentDto getCommentPublicById(Long id);

    CommentDto updateComment(Long userId, Long commentId, UpdateUserCommentDto userCommentDto);

    CommentDto updateCommentByAdmin(UpdateAdminCommentDto updateAdminComment, Long commentId);

    CommentDto findUserCommentById(Long userId, Long eventId);

    CommentDto findCommentById(Long id);

}
