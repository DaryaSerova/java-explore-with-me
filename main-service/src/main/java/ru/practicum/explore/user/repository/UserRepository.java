package ru.practicum.explore.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "FROM User u WHERE id in ( :ids )")
    Page<User> findUsersById(@Param("ids") List<Long> ids, Pageable pageable);

    List<User> findByName(String name);
}
