package ru.practicum.explore.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.comment.StateComment;
import ru.practicum.explore.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentByIdAndWriterId(Long commentId, Long userId);

    @Query(value = "FROM Comment com WHERE com.state = :state ")
    Page<Comment> findAllByState(@Param("state") StateComment state, Pageable page);
}
