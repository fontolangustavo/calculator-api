package com.fontolan.calculator.application.usecases.auth;

import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;

public interface LoginUseCase {
    JwtResponse execute(LoginRequest request);
}