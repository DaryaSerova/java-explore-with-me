package ru.practicum.explore.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.user.dto.NewUserRequestDto;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserRequestDto newUserRequestDto);

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);
}
