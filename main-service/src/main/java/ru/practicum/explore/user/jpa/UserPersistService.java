package ru.practicum.explore.user.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistService {

    Page<User> getUsers(List<Long> ids, int from, int size);

    User createUser(User user);

    void deleteUserById(Long id);

    Optional<User> findUserById(Long id);

    List<User> findUsersByName(String name);
}
