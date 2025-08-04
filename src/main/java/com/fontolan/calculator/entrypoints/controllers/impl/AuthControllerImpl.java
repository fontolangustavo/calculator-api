package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.application.usecases.auth.LoginUseCase;
import com.fontolan.calculator.application.usecases.auth.RegisterUseCase;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.controllers.AuthController;
import com.fontolan.calculator.entrypoints.mapper.UserMapper;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.entrypoints.response.UserResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class AuthControllerImpl implements AuthController {
    private static final Logger log = getLogger(AuthControllerImpl.class);
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final UserMapper userMapper;

    public AuthControllerImpl(LoginUseCase loginUseCase, RegisterUseCase registerUseCase, UserMapper userMapper) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<JwtResponse> login(@Valid LoginRequest request) {
        log.info("[Auth] Login attempt for username: {}", request.getUsername());

        JwtResponse response = loginUseCase.execute(request);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> register(RegisterRequest request) {
        log.info("[Auth] Register new user with username: {}", request.getUsername());
        User user = registerUseCase.execute(request);

        UserResponse response = userMapper.toResponse(user);

        return ResponseEntity.accepted().body(response);
    }
}
