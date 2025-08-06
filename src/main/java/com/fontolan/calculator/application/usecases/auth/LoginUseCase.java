package com.fontolan.calculator.application.usecases.auth;

import com.fontolan.calculator.entrypoints.request.LoginRequest;

public interface LoginUseCase {
    String execute(LoginRequest request);
}