package com.gpb.service;

import com.gpb.entity.Response;

public interface AccountService {

    Response createAccount(long user, String accountType);
}
