package com.gpb.repository;

import com.gpb.converter.UserIdConverter;
import com.gpb.dto.UserDto;
import com.gpb.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Repository
@AllArgsConstructor
public class UserRepositoryStub implements UserRepository {
    private final Set<User> users = new CopyOnWriteArraySet<>();
    private final UserIdConverter userIdConverter;

    @Override
    public Optional<User> findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public UserDto getUser(long id) {
        String uuid = userIdConverter.convertToUUID(String.valueOf(id));

        Optional<User> userOptional = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();

        return userOptional.map(user -> new UserDto(uuid, user.getName()))
                .orElse(null);
    }
}
