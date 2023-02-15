package ru.practicum.explore.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentDto;
import ru.practicum.explore.comment.dto.UpdateAdminCommentDto;
import ru.practicum.explore.comment.dto.UpdateUserCommentDto;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.location.mapper.LocationMapper;
import ru.practicum.explore.user.dto.UserShortDto;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LocationMapper.class})
public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", source = "commentDto.id")
    Comment toComment(CommentDto commentDto);

    @Mapping(target = "id", source = "comment.id")
    CommentDto toCommentDto(Comment comment, UserShortDto userShort, EventShortDto eventShort);

    @Mapping(target = "writerId", source = "userId")
    @Mapping(target = "eventId", source = "newCommentDto.eventId")
    Comment toCommentWithUserIdAndEventId(Long userId, Long eventId, NewCommentDto newCommentDto);

    void mergeToComment(UpdateUserCommentDto userCommentDto, @MappingTarget Comment comment);

    void mergeToCommentAdmin(UpdateAdminCommentDto adminCommentDto, @MappingTarget Comment comment);

}
