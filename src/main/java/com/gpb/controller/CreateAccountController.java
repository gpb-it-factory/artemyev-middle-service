package com.gpb.controller;

import com.gpb.entity.AccountRequestDto;
import com.gpb.entity.Response;
import com.gpb.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> createAccount(@PathVariable("id") long userId, @RequestBody @Valid AccountRequestDto request) {
        Response response = accountService.createAccount(userId, request.getAccountName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if ("User already has an account".equals(response.getMessage())) {
            return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(response, headers, HttpStatus.NO_CONTENT);
        }
    }
}
