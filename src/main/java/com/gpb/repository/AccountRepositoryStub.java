package com.gpb.repository;

import com.gpb.entity.Account;
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
}
