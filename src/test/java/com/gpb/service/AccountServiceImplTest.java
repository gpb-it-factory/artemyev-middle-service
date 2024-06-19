package com.gpb.service;

import com.gpb.entity.Account;
import com.gpb.dto.ResponseDto;
import com.gpb.exception.UserAlreadyHasAccountException;
import com.gpb.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Test createAccount when user does not have an account")
    void testCreateAccountWhenUserDoesNotHaveAccountThenSaveAccountAndReturnNoContent() {

        long userId = 1L;
        String accountType = "My first awesome account";
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ResponseDto responseDto = accountService.createAccount(userId, accountType);


        verify(accountRepository, times(1)).saveAccount(userId, accountType);
        assertEquals("Account created successfully", responseDto.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when user already has an account")
    void testCreateAccountWhenUserAlreadyHasAccountThenReturnInternalServerError() {

        long userId = 1L;
        String accountType = "My first awesome account";
        Account existingAccount = new Account(UUID.randomUUID(), userId, BigDecimal.valueOf(5000), "My first awesome account");
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(existingAccount));

        UserAlreadyHasAccountException exception = assertThrows(UserAlreadyHasAccountException.class, () -> {
            accountService.createAccount(userId, accountType);
        });
        verify(accountRepository, never()).saveAccount(anyLong(), anyString());
        assertEquals("User already has an account", exception.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when an exception occurs during account creation")
    void testCreateAccountWhenExceptionOccursThenThrowRuntimeException() {

        long userId = 1L;
        String accountType = "My first awesome account";
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(accountRepository).saveAccount(userId, accountType);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.createAccount(userId, accountType);
        });

        assertEquals("Database error", exception.getMessage());
        verify(accountRepository, times(1)).saveAccount(userId, accountType);
    }
}
