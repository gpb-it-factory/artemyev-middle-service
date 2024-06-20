package com.gpb.service;

import com.gpb.dto.UserDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.RequestDto;
import com.gpb.entity.User;
import com.gpb.exception.DatabaseConnectionFailureException;
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
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
    }

    @Override
    public BackendResponse saveUser(RequestDto request) {
        BackendResponse backendResponse = new BackendResponse();
        try {
            User user = new User(request.getUserId(), request.getUserName());
            if (userRepository.findById(user.getId()).isPresent()) {
                backendResponse.setSuccess(false);
                return backendResponse;
            }
            userRepository.save(user);
            backendResponse.setSuccess(true);
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
        return backendResponse;
    }

    @Override
    public UserDto getUser(long id) {
        try {
            return userRepository.getUser(id);
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
    }
}
