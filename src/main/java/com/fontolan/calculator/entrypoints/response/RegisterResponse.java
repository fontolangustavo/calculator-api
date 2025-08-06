package com.fontolan.calculator.entrypoints.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private String token;
    private UserResponse user;
}
