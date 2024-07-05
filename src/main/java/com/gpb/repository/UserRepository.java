package com.gpb.repository;

import com.gpb.dto.UserDto;
import com.gpb.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    void save(User user);

    UserDto getUser(long id);
}
