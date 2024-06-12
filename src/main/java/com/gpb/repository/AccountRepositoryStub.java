package com.gpb.repository;

import com.gpb.entity.Account;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountRepositoryStub implements AccountRepository {
    private final Map<Long, Object> accountData = new HashMap<>();

    @Override
    public Account findByUserId(long id) {
        if (accountData.containsKey(id)) {
            return (Account) accountData.get(id);
        }
        return null;
    }

    @Override
    public void save(long userId, String accountType) {
        Account account = new Account();
        account.setId(1L);
        account.setClientId(userId);
        account.setBalance(BigDecimal.valueOf(5000));
        account.setAccountType(accountType);
        accountData.put(userId, account);
    }
}
