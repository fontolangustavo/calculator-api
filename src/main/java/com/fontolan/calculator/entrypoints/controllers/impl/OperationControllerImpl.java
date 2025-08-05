package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.application.usecases.operation.ListOperationsUseCase;
import com.fontolan.calculator.application.usecases.operation.PerformOperationUseCase;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.controllers.OperationController;
import com.fontolan.calculator.entrypoints.mapper.OperationTypeMapper;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class OperationControllerImpl implements OperationController {

    private static final Logger log = getLogger(OperationControllerImpl.class);
    private final PerformOperationUseCase performOperationUseCase;
    private final ListOperationsUseCase listOperationsUseCase;
    private final OperationTypeMapper operationTypeMapper;

    public OperationControllerImpl(PerformOperationUseCase performOperationUseCase, ListOperationsUseCase listOperationsUseCase, OperationTypeMapper operationTypeMapper) {
        this.performOperationUseCase = performOperationUseCase;
        this.listOperationsUseCase = listOperationsUseCase;
        this.operationTypeMapper = operationTypeMapper;
    }

    @Override
    public ResponseEntity<OperationResponse> executeOperation(@Valid OperationRequest request) {
        log.info("[Operation] Executing operation of type: {} with operands: {}", request.getType(), request.getOperands());

        OperationResponse response = performOperationUseCase.execute(request, "teste");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<OperationTypeResponse>> listAvailableOperations(@Valid OperationFilterRequest request) {
        log.info("[Operation] Listing available operation types with request: {}", request);

        Page<Operation> operations = listOperationsUseCase.execute(request);

        Page<OperationTypeResponse> response = operations.map(operationTypeMapper::toResponse);

        return ResponseEntity.ok(response);
    }
}
