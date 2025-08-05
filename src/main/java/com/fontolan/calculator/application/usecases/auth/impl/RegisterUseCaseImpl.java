package com.fontolan.calculator.application.usecases.auth.impl;

import com.fontolan.calculator.application.usecases.auth.RegisterUseCase;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegisterUseCaseImpl implements RegisterUseCase {

    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;

    public RegisterUseCaseImpl(UserDataProvider userDataProvider, PasswordEncoder passwordEncoder) {
        this.userDataProvider = userDataProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(RegisterRequest request) {
        var existingUser = userDataProvider.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new BusinessException("Username already exists: " + request.getUsername());
        }

        String cryptPassword = passwordEncoder.encode(request.getPassword());

        var user = new User(null, request.getUsername(), cryptPassword, UserStatus.ACTIVE, BigDecimal.TEN);

        return userDataProvider.save(user);
    }
}
