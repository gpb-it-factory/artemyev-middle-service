package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.User;
import com.gpb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L);
    }

    @Test
    public void testSaveUserWhenUserSavedThenSuccessIsTrue() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        BackendResponse backendResponse = userService.saveUser(user);

       Assertions.assertTrue(backendResponse.isSuccess());
    }

    @Test
    public void testSaveUserWhenUserNotSavedThenSuccessIsFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        BackendResponse backendResponse = userService.saveUser(user);

        Assertions.assertFalse(backendResponse.isSuccess());
    }
}