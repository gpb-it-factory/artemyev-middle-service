package com.gpb.service;

import com.gpb.entity.Response;
import com.gpb.repository.AccountRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ResponseEntity<?> createAccount(long userId, String accountType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            if (accountRepository.findByUserId(userId) != null) {
                return new ResponseEntity<>(new Response("User already has an account"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            accountRepository.save(userId, accountType);
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }
}
