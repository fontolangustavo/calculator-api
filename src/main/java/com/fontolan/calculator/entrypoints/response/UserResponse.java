package com.fontolan.calculator.entrypoints.response;

import com.fontolan.calculator.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private UserStatus status;
}
