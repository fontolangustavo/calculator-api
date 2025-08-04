package com.fontolan.calculator.application.usecases.auth.impl;

import com.fontolan.calculator.application.usecases.auth.LoginUseCase;
import com.fontolan.calculator.domain.exception.InvalidCredentialsException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserDataProvider userDataProvider;

    public LoginUseCaseImpl(UserDataProvider userDataProvider) {
        this.userDataProvider = userDataProvider;
    }

    @Override
    public JwtResponse execute(LoginRequest request) {
        User user = userDataProvider.findByUsername(request.getUsername());

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = "mocked-jwt-token";

        return new JwtResponse(token);
    }
}
