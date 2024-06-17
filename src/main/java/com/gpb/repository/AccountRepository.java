package com.gpb.repository;

import com.gpb.entity.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByUserId(long id);

    void saveAccount(long userId, String accountType);
}
