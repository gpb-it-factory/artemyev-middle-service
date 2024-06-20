package com.gpb.service;

import com.gpb.dto.AccountResponseDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.ResponseDto;
import com.gpb.exception.DatabaseConnectionFailureException;
import com.gpb.exception.UserAlreadyHasAccountException;
import com.gpb.repository.AccountRepository;
import org.springframework.stereotype.Service;

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
            accountRepository.saveAccount(userId, accountType);
            return new ResponseDto("Account created successfully");
        }
        throw new UserAlreadyHasAccountException("User already has an account");
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
}
