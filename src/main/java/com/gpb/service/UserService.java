package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    BackendResponse saveUser(User user);
    ResponseEntity<?> processBackendResponse(User user, BackendResponse backendResponse);
}
