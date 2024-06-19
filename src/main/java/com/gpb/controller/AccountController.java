package com.gpb.controller;

import com.gpb.dto.AccountRequestDto;
import com.gpb.dto.AccountResponseDto;
import com.gpb.dto.ResponseDto;
import com.gpb.exception.UserNotFoundException;
import com.gpb.service.AccountService;
import com.gpb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/")
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/users/{id}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable("id") long userId, @RequestBody @Valid AccountRequestDto accountRequestDto) {
        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException("User doesn't exist, please register first");
        }
        ResponseDto response = accountService.createAccount(userId, accountRequestDto.getAccountName());
        if (response.getMessage().equals("Account created successfully")) {
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<?> getUserAccounts(@PathVariable("id") long userId) {
        List<AccountResponseDto> accounts = accountService.getUserAccounts(userId);

        if (accounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        }
    }
}
