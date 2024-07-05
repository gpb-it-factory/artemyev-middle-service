package com.gpb.repository;

import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.Account;
import com.gpb.dto.AccountResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByUserId(long id);

    void saveAccount(long userId, String accountType);

    List<AccountResponseDto> getByUserId(long id);

    TransferResponseDto decreaseBalance(long userIdFrom, long userIdTo, BigDecimal amount);
}
