package com.fontolan.calculator.application.usecases.auth;

import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;

public interface RegisterUseCase {
    User execute(RegisterRequest request);
}
