package com.gpb.service;

import com.gpb.dto.AccountResponseDto;
import com.gpb.entity.BackendResponse;
import com.gpb.dto.ResponseDto;

import java.util.List;

public interface AccountService {

    ResponseDto createAccount(long user, String accountType);

    BackendResponse findByUserId(long id);

    List<AccountResponseDto> getUserAccounts(long userId);
}
