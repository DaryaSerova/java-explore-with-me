package ru.practicum.explore.comment.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.comment.StateComment;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.comment.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentPersistServiceImpl implements CommentPersistService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findUserCommentById(Long userId, Long commentId) {
        return commentRepository.findCommentByIdAndWriterId(userId, commentId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<Comment> getCommentsPublic(Boolean sort, int from, int size) {

        if (sort) {
            return commentRepository.findAllByState(StateComment.PUBLISHED,
                    PageRequest.of(from, size, Sort.by("created").descending()));
        }
        return commentRepository.findAllByState(StateComment.PUBLISHED,
                PageRequest.of(from, size, Sort.by("created").ascending()));
    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }
}
