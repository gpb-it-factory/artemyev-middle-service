package com.gpb.service;

import com.gpb.entity.BackendResponse;
import com.gpb.entity.RequestDto;

public interface UserService {
    BackendResponse saveUser(RequestDto request);
}
