package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/operations")
public interface OperationController {

    @PostMapping
    ResponseEntity<OperationResponse> executeOperation(@RequestBody OperationRequest request);

    @GetMapping("/types")
    ResponseEntity<?> listAvailableOperations();
}