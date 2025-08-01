package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.entrypoints.controllers.AuthController;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class AuthControllerImpl implements AuthController {

    private static final Logger log = getLogger(AuthControllerImpl.class);

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest request) {
        log.info("[Auth] Login attempt for username: {}", request.getUsername());

        String mockToken = "mocked-jwt-token";
        return ResponseEntity.ok(new JwtResponse(mockToken));
    }
}
