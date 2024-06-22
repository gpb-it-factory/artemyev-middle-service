package com.gpb.repository;

import com.gpb.dto.TransferResponseDto;
import com.gpb.entity.Account;
import com.gpb.dto.AccountResponseDto;
import com.gpb.exception.AccountNotFoundException;
import com.gpb.exception.NotEnoughFundsException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class AccountRepositoryStub implements AccountRepository {
    private final List<Account> accountData = new CopyOnWriteArrayList<>();

    @Override
    public Optional<Account> findByUserId(long id) {
        return accountData.stream()
                .filter(account -> account.getClientId() == id)
                .findFirst();
    }

    @Override
    public void saveAccount(long userId, String accountType) {
        Account account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setClientId(userId);
        account.setBalance(BigDecimal.valueOf(5000));
        account.setAccountType(accountType);
        accountData.add(account);
    }

    @Override
    public List<AccountResponseDto> getByUserId(long id) {
        return accountData.stream()
                .filter(account -> account.getClientId() == id)
                .map(account -> new AccountResponseDto(account.getAccountId().toString(), account.getAccountType(), account.getBalance()))
                .toList();
    }

    @Override
    public TransferResponseDto decreaseBalance(long userIdFrom, long userIdTo, BigDecimal amount) {
        TransferResponseDto transferResponseDto = new TransferResponseDto();
        Account fromAccount = accountData.stream()
                .filter(account -> account.getClientId() == userIdFrom)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account not found for userId: " + userIdFrom));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughFundsException("Insufficient funds for userId: " + userIdFrom);
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        transferResponseDto.setTransferId(UUID.randomUUID().toString());
        increaseBalance(userIdTo, amount);
        return transferResponseDto;
    }

    private void increaseBalance(long userIdTo, BigDecimal amount) {
        Account toAccount = accountData.stream()
                .filter(account -> account.getClientId() == userIdTo)
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account not found for userId: " + userIdTo));
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }
}
