package com.gpb.controller;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.RequestDto;
import com.gpb.entity.ResponseDto;
import com.gpb.service.UserService;
import org.springframework.http.HttpStatus;
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

        if (backendResponse != null && backendResponse.isSuccess()) {
            ResponseDto responseDto = new ResponseDto("Пользователь успешно создан");
            return new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new ResponseDto("Пользователь с таким id уже существует"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}