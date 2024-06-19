package com.gpb.service;

import com.gpb.dto.UserDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.RequestDto;
import com.gpb.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(long id);

    BackendResponse saveUser(RequestDto request);

    UserDto getUser(long id);
}
