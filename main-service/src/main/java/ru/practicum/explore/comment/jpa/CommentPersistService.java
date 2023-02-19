package ru.practicum.explore.comment.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.comment.model.Comment;

import java.util.Optional;

public interface CommentPersistService {

    Comment addComment(Comment comment);

    void deleteComment(Long commentId);

    Page<Comment> getCommentsPublic(String[] sort, int from, int size);

    Optional<Comment> findCommentById(Long id);

    Comment updateComment(Comment comment);

    Comment findUserCommentById(Long userId, Long commentId);

}
