package ru.practicum.explore.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.admin.StateAction;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.comment.StateComment;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentDto;
import ru.practicum.explore.comment.dto.UpdateAdminCommentDto;
import ru.practicum.explore.comment.dto.UpdateUserCommentDto;
import ru.practicum.explore.comment.jpa.CommentPersistService;
import ru.practicum.explore.comment.mapper.CommentMapper;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.jpa.EventPersistService;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.exceptions.ConflictException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentPersistService commentPersistService;

    private final CommentMapper commentMapper;

    private final EventMapper eventMapper;

    private final EventPersistService eventPersistService;

    private final UserService userService;

    private final CategoryService categoryService;


    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {

        var eventOpt = eventPersistService.findEventById(eventId);

         if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                      String.format("Event with id = %s was not found", eventId));
        }

        var event = eventOpt.get();

        if (event != null && !event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                                        "Cannot publish the event because it's not in the right state: " +
                                        "PENDING or CANCELED");
        }

        var comment = commentMapper.toCommentWithUserIdAndEventId(userId, eventId, newCommentDto);

        comment.setState(StateComment.PENDING);

        comment = commentPersistService.addComment(comment);

        var user = userService.getUserShortById(event.getInitiatorId());
        var category = categoryService.getCategoryById(event.getCategoryId());
        var eventResult = eventMapper.toEventShortDto(event, category, user);

        return commentMapper.toCommentDto(comment, user, eventResult);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {

        var comment = findUserCommentById(userId, commentId);

        if (comment.getState().equals(StateComment.PUBLISHED)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                       "Cannot delete the comment because it's not in the right state: PUBLISHED");
        } else {
            commentPersistService.deleteComment(commentId);
        }
    }

    @Override
    public List<CommentDto> getCommentsPublic(String[] sort, int from, int size) {

        var comments = commentPersistService.getCommentsPublic(sort, from, size);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        var commentsList = comments.stream()
                .map(comment -> {
                    var userShort = userService.getUserShortById(comment.getWriterId());
                    var event = eventPersistService.findEventById(comment.getEventId()).get();
                    var categoryDto = categoryService.getCategoryById(event.getCategoryId());
                    var eventShort = eventMapper.toEventShortDto(event, categoryDto, userShort);
                    return commentMapper.toCommentDto(comment, userShort, eventShort);
                }).collect(Collectors.toList());

        return commentsList;
    }

    @Override
    public CommentDto getCommentPublicById(Long id) {

        var commentOpt = commentPersistService.findCommentById(id);

        if (commentOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                      String.format("Comment with id = %s was not found", id));
        }
        var comment = commentOpt.get();

        if (!comment.getState().equals(StateComment.PUBLISHED)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                      "Cannot delete the comment because it's not in the right state: PENDING or CANCELED.");
        }

        var user = userService.getUserShortById(comment.getWriterId());
        var event = eventPersistService.findEventById(comment.getEventId()).get();
        var category = categoryService.getCategoryById(event.getCategoryId());
        var eventResult = eventMapper.toEventShortDto(event, category, user);

        return commentMapper.toCommentDto(comment, user, eventResult);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateUserCommentDto userCommentDto) {

        var comment = commentPersistService.findUserCommentById(userId, commentId);
        var stateComment = comment.getState();

        if (stateComment.equals(StateComment.PUBLISHED)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                                        "Only pending or canceled comment can be changed");
        }

        commentMapper.mergeToComment(userCommentDto, comment);
        comment.setState(getStateComment(comment, userCommentDto.getStateAction()));
        comment.setWriterId(userId);

        commentPersistService.updateComment(comment);

        var user = userService.getUserShortById(comment.getWriterId());
        var event = eventPersistService.findEventById(comment.getEventId()).get();
        var category = categoryService.getCategoryById(event.getCategoryId());
        var eventResult = eventMapper.toEventShortDto(event, category, user);

        return commentMapper.toCommentDto(comment, user, eventResult);
    }

    @Override
    public CommentDto updateCommentByAdmin(UpdateAdminCommentDto updateAdminComment, Long commentId) {

        var comment = commentPersistService.findCommentById(commentId).get();

        if (comment == null) {
            throw new NotFoundException("The required object was not found.",
                      String.format("Comment with id = %s was not found", commentId));
        }

        if (updateAdminComment.getStateAction().equals(StateAction.PUBLISH_COMMENT)) {

            if (comment.getState().equals(StateComment.PUBLISHED) || comment.getState().equals(StateComment.CANCELED)) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                          "Cannot publish the comment because it's not in the right state: PUBLISHED");
            }

            commentMapper.mergeToCommentAdmin(updateAdminComment, comment);

            comment.setState(StateComment.PUBLISHED);
            commentPersistService.updateComment(comment);

            var user = userService.getUserShortById(comment.getWriterId());
            var event = eventPersistService.findEventById(comment.getEventId()).get();
            var category = categoryService.getCategoryById(event.getCategoryId());
            var eventResult = eventMapper.toEventShortDto(event, category, user);

            return commentMapper.toCommentDto(comment, user, eventResult);
        }

        if (updateAdminComment.getStateAction().equals(StateAction.REJECT_COMMENT)) {

            if (comment.getState().equals(StateComment.PUBLISHED)) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                          "Cannot publish the comment because it's not in the right state: PUBLISHED");
            }
            commentMapper.mergeToCommentAdmin(updateAdminComment, comment);
            comment.setState(StateComment.CANCELED);

            commentPersistService.updateComment(comment);

            var user = userService.getUserShortById(comment.getWriterId());
            var event = eventPersistService.findEventById(comment.getEventId()).get();
            var category = categoryService.getCategoryById(event.getCategoryId());
            var eventResult = eventMapper.toEventShortDto(event, category, user);

            return commentMapper.toCommentDto(comment, user, eventResult);
        }

        throw new ConflictException("For the requested operation the conditions are not met.",
                  "Cannot publish the comment because it's not in the right state: PUBLISHED");
    }

    @Override
    public CommentDto findUserCommentById(Long userId, Long commentId) {

        var comment = commentMapper.toCommentDto(
                commentPersistService.findUserCommentById(userId, commentId));
        if (comment == null) {
            throw new NotFoundException("The required object was not found.",
                      String.format("Comment with id = %s was not found", commentId));
        }

        return comment;
    }

    @Override
    public CommentDto findCommentById(Long id) {

        var comment = commentPersistService.findCommentById(id).get();

        var event = eventPersistService.findEventById(comment.getEventId()).get();
        var categoryDto = categoryService.getCategoryById(event.getCategoryId());
        var userShort = userService.getUserShortById(event.getInitiatorId());
        var eventShort = eventMapper.toEventShortDto(event, categoryDto, userShort);

        return commentMapper.toCommentDto(comment, userShort, eventShort);
    }

    private StateComment getStateComment(Comment comment, StateAction stateAction) {

        switch (stateAction) {
            case CANCEL_REVIEW:
                return StateComment.CANCELED;
            case PUBLISH_COMMENT:
                return StateComment.PUBLISHED;
            case SEND_TO_REVIEW:
                return StateComment.PENDING;
            default:
                return comment.getState();

        }
    }
}
