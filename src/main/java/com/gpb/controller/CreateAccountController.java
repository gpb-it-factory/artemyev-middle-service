package com.gpb.controller;

import com.gpb.entity.AccountRequest;
import com.gpb.entity.Request;
import com.gpb.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v2/")
public class CreateAccountController {
    private final AccountService accountService;

    public CreateAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/users/{id}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable("id") long userId, @RequestBody @Valid AccountRequest request) {
        return accountService.createAccount(userId, request.getAccountName());
    }
}
