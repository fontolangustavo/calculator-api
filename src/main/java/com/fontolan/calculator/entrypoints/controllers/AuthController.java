package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request);
}
