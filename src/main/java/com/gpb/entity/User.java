package com.gpb.entity;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Min(value = 1, message = "ID must be greater than 0")
    private long id;

    private String name;
}
