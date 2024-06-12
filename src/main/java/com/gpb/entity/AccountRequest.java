package com.gpb.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private long userId;
    @NotEmpty(message = "Account name cannot be empty")
    private String accountName;
}
