package com.fontolan.calculator.application.usecases.auth;

import com.fontolan.calculator.domain.model.User;

public interface GetUserUseCase {
    User execute(String username);
}
