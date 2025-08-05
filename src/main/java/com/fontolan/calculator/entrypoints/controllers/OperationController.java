package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/operations")
public interface OperationController {

    @PostMapping
    ResponseEntity<OperationResponse> executeOperation(@AuthenticationPrincipal String username,
                                                       @Valid @RequestBody OperationRequest request
    );

    @GetMapping("/types")
    ResponseEntity<Page<OperationTypeResponse>> listAvailableOperations(@Valid OperationFilterRequest request);
}