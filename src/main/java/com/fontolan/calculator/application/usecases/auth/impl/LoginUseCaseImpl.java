package com.fontolan.calculator.application.usecases.auth.impl;

import com.fontolan.calculator.application.usecases.auth.LoginUseCase;
import com.fontolan.calculator.domain.exception.InvalidCredentialsException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserDataProvider userDataProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginUseCaseImpl(UserDataProvider userDataProvider, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userDataProvider = userDataProvider;
        this.passwordEncoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public JwtResponse execute(LoginRequest request) {
        User user = userDataProvider.findByUsername(request.getUsername());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new JwtResponse(token);
    }
}
