package com.gpb.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private long id;
    @Min(value = 1, message = "ID must be greater than 0")
    private long clientId;
    @Min(value = 1, message = "ID must be greater than 0")
    private BigDecimal balance;
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String accountType;
}
