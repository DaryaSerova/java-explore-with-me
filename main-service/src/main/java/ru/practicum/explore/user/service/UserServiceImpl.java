package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.exceptions.BadRequestException;
import ru.practicum.explore.exceptions.ConflictException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.user.dto.NewUserRequestDto;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.jpa.UserPersistService;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserPersistService userPersistService;
    private final UserMapper userMapper;

    private final EventMapper eventMapper;
    private Pattern emailPattern = Pattern.compile("^.+@.+\\..+$");

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {

        var users = userPersistService.getUsers(ids, from, size).getContent();

        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(el -> userMapper.toUserDto(el)).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequestDto newUserRequestDto) {


        if (newUserRequestDto.getName() == null) {
            throw new BadRequestException("Bad request body", "User name is empty");
        }
        var users = userPersistService.findUsersByName(newUserRequestDto.getName());
        if (!CollectionUtils.isEmpty(users)) {
            throw new ConflictException("Integrity constraint has been violated.", "User already exist ");
        }
        var user = userMapper.toUser(newUserRequestDto);

        if (!emailPattern.matcher(user.getEmail()).matches()) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_email]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        userPersistService.createUser(user);

        return userMapper.toUserDto(userPersistService.createUser(userMapper.toUser(newUserRequestDto)));
    }

    @Override
    public void deleteUserById(Long id) {

        getUserShortById(id);

        log.debug("User with id {} was found", id);

        userPersistService.deleteUserById(id);

        log.debug("User with id {} was deleted", id);
    }

    public UserShortDto getUserShortById(Long id) {

        Optional<User> user = userPersistService.findUserById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("The required object was not found.", "User with %id was not found." + id);
        }

        return userMapper.toUserShortDto(user.get());
    }
}