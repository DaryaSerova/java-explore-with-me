package ru.practicum.explore.comment.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentPersistService {

    Comment addComment(Comment comment);

    Comment findUserCommentById(Long userId, Long commentId);

    void deleteComment(Long commentId);

    Page<Comment> getCommentsPublic(Boolean sort, int from, int size);

    Optional<Comment> findCommentById(Long id);
}
