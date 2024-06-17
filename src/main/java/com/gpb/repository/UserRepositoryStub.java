package com.gpb.repository;

import com.gpb.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Repository
public class UserRepositoryStub implements UserRepository {
    private final Set<User> users = new CopyOnWriteArraySet<>();
    @Override
    public Optional<User> findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
