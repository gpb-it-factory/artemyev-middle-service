package com.gpb.controller;

import com.gpb.entity.*;
import com.gpb.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody RequestDto request) {
        BackendResponse backendResponse = userService.saveUser(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (backendResponse != null && backendResponse.isSuccess()) {
            Response response = new Response("Пользователь успешно создан");
            return new ResponseEntity<>(response, headers, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new Response("Пользователь с таким id уже существует"),
                    headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}