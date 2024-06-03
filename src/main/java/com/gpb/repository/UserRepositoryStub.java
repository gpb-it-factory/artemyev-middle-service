package com.gpb.repository;

import com.gpb.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryStub implements UserRepository {
    private List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }
}
