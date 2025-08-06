package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.entrypoints.response.RegisterResponse;
import com.fontolan.calculator.entrypoints.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request);

    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request);

    @GetMapping("/me")
    ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal String username
    );
}
