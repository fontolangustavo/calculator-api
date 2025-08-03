package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.application.usecases.auth.LoginUseCase;
import com.fontolan.calculator.entrypoints.controllers.AuthController;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class AuthControllerImpl implements AuthController {
    private static final Logger log = getLogger(AuthControllerImpl.class);
    private final LoginUseCase loginUseCase;

    public AuthControllerImpl(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public ResponseEntity<JwtResponse> login(@Valid LoginRequest request) {
        log.info("[Auth] Login attempt for username: {}", request.getUsername());

        JwtResponse response = loginUseCase.execute(request);

        return ResponseEntity.ok(response);
    }
}
