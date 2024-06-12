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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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


        ResponseEntity<?> response = accountService.createAccount(userId, accountType);


        verify(accountRepository, times(1)).save(userId, accountType);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test createAccount when user already has an account")
    void testCreateAccountWhenUserAlreadyHasAccountThenReturnInternalServerError() {

        long userId = 1L;
        String accountType = "My first awesome account";
        Account existingAccount = new Account();
        when(accountRepository.findByUserId(userId)).thenReturn(existingAccount);


        ResponseEntity<?> response = accountService.createAccount(userId, accountType);


        verify(accountRepository, never()).save(anyLong(), anyString());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("User already has an account", ((Response) response.getBody()).getMessage());
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
