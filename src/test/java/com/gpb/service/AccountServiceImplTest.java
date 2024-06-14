package com.gpb.service;

import com.gpb.entity.Account;
import com.gpb.entity.Response;
import com.gpb.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(accountRepository.findByUserId(userId)).thenReturn(null);


        Response response = accountService.createAccount(userId, accountType);


        verify(accountRepository, times(1)).save(userId, accountType);
        assertEquals("Account created successfully", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when user already has an account")
    void testCreateAccountWhenUserAlreadyHasAccountThenReturnInternalServerError() {

        long userId = 1L;
        String accountType = "My first awesome account";
        Account existingAccount = new Account();
        when(accountRepository.findByUserId(userId)).thenReturn(existingAccount);


        Response response = accountService.createAccount(userId, accountType);


        verify(accountRepository, never()).save(anyLong(), anyString());
        assertEquals("User already has an account", response.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when an exception occurs during account creation")
    void testCreateAccountWhenExceptionOccursThenThrowRuntimeException() {

        long userId = 1L;
        String accountType = "My first awesome account";
        when(accountRepository.findByUserId(userId)).thenReturn(null);
        doThrow(new RuntimeException("Database error")).when(accountRepository).save(userId, accountType);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.createAccount(userId, accountType);
        });

        assertEquals("Something went wrong", exception.getMessage());
    }
}
