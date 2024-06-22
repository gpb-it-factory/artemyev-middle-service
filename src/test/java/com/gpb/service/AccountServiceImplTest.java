package com.gpb.service;

import com.gpb.dto.AccountResponseDto;
import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.Account;
import com.gpb.dto.ResponseDto;
import com.gpb.entity.BackendResponse;
import com.gpb.exception.DatabaseConnectionFailureException;
import com.gpb.exception.UserAlreadyHasAccountException;
import com.gpb.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;
    private Account existingAccount;
    private long userId;
    private String accountType;
    private long from;
    private long to;
    private BigDecimal amount;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        accountType = "Акционный";
        existingAccount = new Account(UUID.randomUUID(), userId, BigDecimal.valueOf(1000), "Existing Account");

        from = 1L;
        to = 2L;
        amount = BigDecimal.valueOf(1250.50);
    }

    @Test
    @DisplayName("Test createAccount when user does not have an account")
    void testCreateAccountWhenUserDoesNotHaveAccountThenSaveAccountAndReturnNoContent() {

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        ResponseDto responseDto = accountService.createAccount(userId, accountType);

        verify(accountRepository, times(1)).saveAccount(userId, accountType);
        assertEquals("Account created successfully", responseDto.getMessage());
    }

    @Test
    @DisplayName("Test createAccount when user already has an account")
    void testCreateAccountWhenUserAlreadyHasAccountThenReturnInternalServerError() {
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
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
        doThrow(new DatabaseConnectionFailureException("Failed to connect to the database")).when(accountRepository).saveAccount(userId, accountType);

        DatabaseConnectionFailureException exception = assertThrows(DatabaseConnectionFailureException.class, () -> {
            accountService.createAccount(userId, accountType);
        });

        assertEquals("Failed to connect to the database", exception.getMessage());
        verify(accountRepository, times(1)).saveAccount(userId, accountType);
    }

    @Test
    @DisplayName("Test findByUserId when user does not have an account")
    void testFindByUserIdWhenUserDoesNotHaveAccountThenReturnSuccess() {
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        BackendResponse backendResponse = accountService.findByUserId(userId);

        assertTrue(backendResponse.isSuccess());
    }

    @Test
    @DisplayName("Test findByUserId when user already has an account")
    void testFindByUserIdWhenUserHasAccountThenReturnFailure() {
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(existingAccount));

        BackendResponse backendResponse = accountService.findByUserId(userId);

        assertFalse(backendResponse.isSuccess());
    }

    @Test
    @DisplayName("Test findByUserId when exception occurs")
    void testFindByUserIdWhenExceptionOccursThenReturnFailure() {
        when(accountRepository.findByUserId(userId)).thenThrow(new DatabaseConnectionFailureException("Failed to connect to the database"));

        DatabaseConnectionFailureException exception = assertThrows(DatabaseConnectionFailureException.class, () -> {
            accountService.findByUserId(userId);
        });

        assertEquals("Failed to connect to the database", exception.getMessage());
    }

    @Test
    @DisplayName("Test getUserAccounts when user has accounts then return the list of AccountResponseDto")
    void testGetUserAccountsWhenUserHasAccountsThenReturnAccountResponseDtoList() {
        List<AccountResponseDto> accountResponseDtos = Arrays.asList(
                new AccountResponseDto("1", "Account 1", BigDecimal.valueOf(1000)),
                new AccountResponseDto("2", "Account 2", BigDecimal.valueOf(2000))
        );
        when(accountRepository.getByUserId(userId)).thenReturn(accountResponseDtos);

        List<AccountResponseDto> result = accountService.getUserAccounts(userId);

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getAccountId());
        assertEquals("Account 1", result.get(0).getAccountName());
        assertEquals(BigDecimal.valueOf(1000), result.get(0).getAmount());
        assertEquals("2", result.get(1).getAccountId());
        assertEquals("Account 2", result.get(1).getAccountName());
        assertEquals(BigDecimal.valueOf(2000), result.get(1).getAmount());
    }

    @Test
    @DisplayName("Test getUserAccounts when user does not have accounts then return an empty list")
    void testGetUserAccountsWhenUserDoesNotHaveAccountsThenReturnEmptyList() {
        when(accountRepository.getByUserId(userId)).thenReturn(Collections.emptyList());

        List<AccountResponseDto> result = accountService.getUserAccounts(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test getUserAccounts when exception occurs during account retrieval then throw the exception")
    void testGetUserAccountsWhenExceptionOccursDuringAccountRetrievalThenThrowException() {
        when(accountRepository.getByUserId(userId)).thenThrow(new DatabaseConnectionFailureException("Failed to connect to the database"));

        DatabaseConnectionFailureException exception = assertThrows(DatabaseConnectionFailureException.class, () -> {
            accountRepository.getByUserId(userId);
        });

        assertEquals("Failed to connect to the database", exception.getMessage());
    }

    @Test
    @DisplayName("Test decreaseBalance when the account balance is successfully decreased")
    void testDecreaseBalanceWhenBalanceIsSuccessfullyDecreasedThenReturnTransferResponseDto() {
        TransferResponseDto transferResponseDto = new TransferResponseDto("Transfer successful");

        when(accountRepository.decreaseBalance(from, to, amount)).thenReturn(transferResponseDto);

        TransferResponseDto result = accountService.decreaseBalance(from, to, amount);

        assertEquals("Transfer successful", result.getTransferId());
        verify(accountRepository, times(1)).decreaseBalance(from, to, amount);
    }

    @Test
    @DisplayName("Test decreaseBalance when a DatabaseConnectionFailureException occurs")
    void testDecreaseBalanceWhenDatabaseConnectionFailureThenThrowException() {
        when(accountRepository.decreaseBalance(from, to, amount)).thenThrow(new DatabaseConnectionFailureException("Failed to connect to the database"));

        DatabaseConnectionFailureException exception = assertThrows(DatabaseConnectionFailureException.class, () -> {
            accountService.decreaseBalance(from, to, amount);
        });

        assertEquals("Failed to connect to the database", exception.getMessage());
    }
}
