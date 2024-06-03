package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.Error;
import com.gpb.entity.Response;
import com.gpb.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public BackendResponse saveUser(User user) {
        BackendResponse backendResponse = new BackendResponse();
        backendResponse.setSuccess(true);
        return backendResponse;
    }

    @Override
    public ResponseEntity<?> processBackendResponse(User user, BackendResponse backendResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (backendResponse != null && backendResponse.isSuccess()) {
            Response response = new Response("User with id: " + user.getId() + " successfully registered!");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } else {
            Error error = new Error("User with id: " + user.getId() + " failed to register!");
            return new ResponseEntity<>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
