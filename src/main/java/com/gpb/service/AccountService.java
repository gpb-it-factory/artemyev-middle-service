package com.gpb.service;

import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<?> createAccount(long user, String accountType);
}
