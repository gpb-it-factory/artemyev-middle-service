package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.Response;
import com.gpb.entity.User;
import com.gpb.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BackendResponse saveUser(User user) {
        BackendResponse backendResponse = new BackendResponse();

        if (userRepository.findById(user.getId()).isPresent()) {
            backendResponse.setSuccess(false);
            return backendResponse;
        }
        userRepository.save(user);
        backendResponse.setSuccess(true);
        return backendResponse;
    }

    @Override
    public ResponseEntity<?> processBackendResponse(User user, BackendResponse backendResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (backendResponse != null && backendResponse.isSuccess()) {
            Response response = new Response("Пользователь успешно создан");
            return new ResponseEntity<>(response, headers, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new Response("Пользователь с id: " + user.getId() + " уже существует"),
                    headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
