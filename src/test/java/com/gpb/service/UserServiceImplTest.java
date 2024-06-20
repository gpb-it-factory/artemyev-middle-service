package com.gpb.service;

import com.gpb.dto.UserDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.RequestDto;
import com.gpb.entity.User;
import com.gpb.exception.DatabaseConnectionFailureException;
import com.gpb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private RequestDto newUser;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        newUser = new RequestDto(1L, "Николай");
        user = new User(1L, "Николай");
        userDto = new UserDto("1", "Николай");
    }

    @Test
    @DisplayName("Test saveUser when user not exists")
    public void testSaveUserWhenUserSavedThenSuccessIsTrue() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        BackendResponse backendResponse = userService.saveUser(newUser);

        Assertions.assertTrue(backendResponse.isSuccess());
    }

    @Test
    @DisplayName("Test saveUser when user already exists")
    public void testSaveUserWhenUserNotSavedThenSuccessIsFalse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        BackendResponse backendResponse = userService.saveUser(newUser);

        Assertions.assertFalse(backendResponse.isSuccess());
    }

    @Test
    @DisplayName("Test findById when user found")
    public void testFindByIdWhenUserFoundThenReturnOptionalWithUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Test findById when user not found")
    public void testFindByIdWhenUserNotFoundThenReturnEmptyOptional() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(1L);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test findById when exception occurs")
    void testFindByUserIdWhenExceptionOccursThenReturnFailure() {
        when(userRepository.findById(1L)).thenThrow(new DatabaseConnectionFailureException("Failed to connect to the database"));

        DatabaseConnectionFailureException exception = assertThrows(DatabaseConnectionFailureException.class, () -> {
            userService.findById(1L);
        });

        assertEquals("Failed to connect to the database", exception.getMessage());
    }

    @Test
    @DisplayName("Test getUser when user found")
    public void testGetUserWhenUserFoundThenReturnUserDto() {
        when(userRepository.getUser(1L)).thenReturn(userDto);

        UserDto result = userService.getUser(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userDto, result);
    }

    @Test
    @DisplayName("Test getUser when user not")
    public void testGetUserWhenUserNotFoundThenReturnNull() {
        when(userRepository.getUser(1L)).thenReturn(null);

        UserDto result = userService.getUser(1L);

        Assertions.assertNull(result);
    }
}