package com.gpb.service;

import com.gpb.dto.ResponseDto;
import com.gpb.dto.TransferRequestDto;
import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.User;
import com.gpb.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransferService transferService;

    private TransferRequestDto validRequest;
    private User validUser;
    private TransferResponseDto validTransferResponse;

    @BeforeEach
    public void setUp() {
        validRequest = new TransferRequestDto(1L, "Иван", new BigDecimal("1250.50"));
        validUser = new User(2L, "Олег");
        validTransferResponse = new TransferResponseDto("transfer-123");
    }

    @Test
    @DisplayName("Test transfer when user exists and transfer successful then return success message")
    public void testTransferWhenUserExistsAndTransferSuccessfulThenReturnSuccessMessage() {
        when(userService.findByUsername("Иван")).thenReturn(Optional.of(validUser));
        when(accountService.decreaseBalance(1L, 2L, new BigDecimal("1250.50"))).thenReturn(validTransferResponse);

        ResponseDto response = transferService.transfer(validRequest);

        assertNotNull(response);
        assertEquals("Transfer successful: transfer-123", response.getMessage());
        verify(userService, times(1)).findByUsername("Иван");
        verify(accountService, times(1)).decreaseBalance(1L, 2L, new BigDecimal("1250.50"));
    }

    @Test
    @DisplayName("Test transfer when user does not exist then throw UserNotFoundException")
    public void testTransferWhenUserDoesNotExistThenThrowUserNotFoundException() {
        when(userService.findByUsername("Иван")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            transferService.transfer(validRequest);
        });

        assertEquals("User doesn't exist, please register first", exception.getMessage());
        verify(userService, times(1)).findByUsername("Иван");
        verify(accountService, never()).decreaseBalance(anyLong(), anyLong(), any(BigDecimal.class));
    }

    @Test
    @DisplayName("Test transfer when transfer fails then throw RuntimeException")
    public void testTransferWhenTransferFailsThenThrowRuntimeException() {
        when(userService.findByUsername("Иван")).thenReturn(Optional.of(validUser));
        when(accountService.decreaseBalance(1L, 2L, new BigDecimal("1250.50"))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transferService.transfer(validRequest);
        });

        assertEquals("Internal server error during transfer", exception.getMessage());
        verify(userService, times(1)).findByUsername("Иван");
        verify(accountService, times(1)).decreaseBalance(1L, 2L, new BigDecimal("1250.50"));
    }
}