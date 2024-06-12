package com.gpb.repository;

import com.gpb.entity.Account;

public interface AccountRepository {
    Account findByUserId(long id);

    void save(long userId, String accountType);
}
