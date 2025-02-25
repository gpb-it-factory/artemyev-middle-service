package com.gpb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {
    private long userId;
    @NotEmpty(message = "Account name cannot be empty")
    private String accountName;
}
