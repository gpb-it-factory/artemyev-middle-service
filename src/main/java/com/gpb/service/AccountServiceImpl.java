package com.gpb.service;

import com.gpb.entity.Response;
import com.gpb.repository.AccountRepository;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Response createAccount(long userId, String accountType) {
        try {
            if (accountRepository.findByUserId(userId) != null) {
                return new Response("User already has an account");
            }

            accountRepository.save(userId, accountType);
            return new Response("Account created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }
}
