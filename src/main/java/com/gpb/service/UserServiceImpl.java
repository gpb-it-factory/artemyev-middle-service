package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.RequestDto;
import com.gpb.entity.User;
import com.gpb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public BackendResponse saveUser(RequestDto request) {
        BackendResponse backendResponse = new BackendResponse();
        User user = new User(request.getUserId(), request.getUserName());
        if (userRepository.findById(user.getId()).isPresent()) {
            backendResponse.setSuccess(false);
            return backendResponse;
        }
        userRepository.save(user);
        backendResponse.setSuccess(true);
        return backendResponse;
    }
}
