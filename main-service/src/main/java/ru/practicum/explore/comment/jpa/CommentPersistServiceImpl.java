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
        return commentRepository.findCommentByIdAndWriterId(commentId, userId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Page<Comment> getCommentsPublic(String[] sort, int from, int size) {
        Sort sortS;
        if (Sort.Direction.DESC.name().equals(sort[1])) {
            sortS = Sort.by(Sort.Direction.DESC, sort[0]);
        } else {
            sortS = Sort.by(Sort.Direction.ASC, sort[0]);
        }
        var page = PageRequest.of(from, size, sortS);
        return commentRepository.findAllByState(StateComment.PUBLISHED, page);

    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
