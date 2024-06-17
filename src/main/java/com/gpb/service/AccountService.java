package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.ResponseDto;

public interface AccountService {

    ResponseDto createAccount(long user, String accountType);
    BackendResponse findByUserId(long id);
}
