package com.gpb.service;

import com.gpb.dto.ResponseDto;
import com.gpb.dto.TransferRequestDto;
import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.User;
import com.gpb.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransferService {
    private final AccountService accountService;
    private final UserService userService;

    public ResponseDto transfer(TransferRequestDto request) {
        long from = request.getFrom();
        BigDecimal amount = request.getAmount();
        Optional<User> to = userService.findByUsername(request.getTo());
        if (to.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist, please register first");
        }
        TransferResponseDto response = accountService.decreaseBalance(from, to.get().getId(), amount);
        if (response != null) {
            return new ResponseDto("Transfer successful: " + response.getTransferId());
        }
        throw new RuntimeException("Internal server error during transfer");
    }
}
