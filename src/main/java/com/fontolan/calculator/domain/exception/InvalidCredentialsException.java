package com.fontolan.calculator.domain.exception;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Invalid credentials", "INVALID_CREDENTIALS");
    }
}
