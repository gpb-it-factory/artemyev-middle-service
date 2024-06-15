package com.gpb.repository;

import com.gpb.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryStub implements UserRepository {
    private final ConcurrentHashMap<Long, String> users = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findById(long id) {
        return users.containsKey(id) ? Optional.of(new User((id), users.get(id))) : Optional.empty();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user.getName());
    }
}
