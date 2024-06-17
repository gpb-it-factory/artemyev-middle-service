package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.ResponseDto;
import com.gpb.exception.UserAlreadyHasAccountException;
import com.gpb.repository.AccountRepository;
import org.springframework.stereotype.Service;


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
        if (accountRepository.findByUserId(id).isPresent()) {
            backendResponse.setSuccess(false);
            return backendResponse;
        }
        backendResponse.setSuccess(true);
        return backendResponse;
    }
}
