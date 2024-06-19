package com.gpb.controller;

import com.gpb.entity.BackendResponse;
import com.gpb.dto.RequestDto;
import com.gpb.dto.ResponseDto;
import com.gpb.exception.UserNotFoundException;
import com.gpb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
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

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") long userId) {
        if (userService.findById(userId).isPresent()) {
            return new ResponseEntity<>(userService.getUser(userId).getId(), HttpStatus.OK);
        }
        throw new UserNotFoundException("User doesn't exist, please register first");
    }
}