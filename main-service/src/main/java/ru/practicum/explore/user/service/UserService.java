package ru.practicum.explore.user.service;

import ru.practicum.explore.user.dto.NewUserRequestDto;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequestDto newUserRequestDto);

    void deleteUserById(Long id);

    UserShortDto getUserShortById(Long id);
}
