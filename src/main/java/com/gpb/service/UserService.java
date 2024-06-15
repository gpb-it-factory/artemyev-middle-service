package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.RequestDto;
import com.gpb.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);
    BackendResponse saveUser(RequestDto request);
}
