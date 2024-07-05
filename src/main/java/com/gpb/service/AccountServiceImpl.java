package com.gpb.service;

import com.gpb.dto.AccountResponseDto;
import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.ResponseDto;
import com.gpb.exception.AccountNotFoundException;
import com.gpb.exception.DatabaseConnectionFailureException;
import com.gpb.exception.NotEnoughFundsException;
import com.gpb.exception.UserAlreadyHasAccountException;
import com.gpb.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ResponseDto createAccount(long userId, String accountType) {
        BackendResponse backendResponse = findByUserId(userId);
        if (backendResponse.isSuccess()) {
            try {
                accountRepository.saveAccount(userId, accountType);
                return new ResponseDto("Account created successfully");
            } catch (Exception e) {
                throw new DatabaseConnectionFailureException("Failed to connect to the database");
            }
        } else {
            throw new UserAlreadyHasAccountException("User already has an account");
        }

    }

    @Override
    public BackendResponse findByUserId(long id) {
        BackendResponse backendResponse = new BackendResponse();
        try {
            if (accountRepository.findByUserId(id).isPresent()) {
                backendResponse.setSuccess(false);
                return backendResponse;
            }
            backendResponse.setSuccess(true);
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
        return backendResponse;
    }

    @Override
    public List<AccountResponseDto> getUserAccounts(long userId) {
        try {
            return accountRepository.getByUserId(userId);
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
    }

    @Override
    public TransferResponseDto decreaseBalance(long userIdFrom, long userIdTo, BigDecimal amount) {
        try {
            return accountRepository.decreaseBalance(userIdFrom, userIdTo, amount);
        } catch (NotEnoughFundsException e) {
            throw new NotEnoughFundsException(e.getMessage());
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DatabaseConnectionFailureException("Failed to connect to the database");
        }
    }
}
