package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.application.usecases.operation.PerformOperationUseCase;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.entrypoints.controllers.OperationController;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class OperationControllerImpl implements OperationController {

    private static final Logger log = getLogger(OperationControllerImpl.class);
    private final PerformOperationUseCase performOperationUseCase;

    public OperationControllerImpl(PerformOperationUseCase performOperationUseCase) {
        this.performOperationUseCase = performOperationUseCase;
    }

    @Override
    public ResponseEntity<OperationResponse> executeOperation(@Valid OperationRequest request) {
        log.info("[Operation] Executing operation of type: {} with operands: {}", request.getType(), request.getOperands());

        OperationResponse response = performOperationUseCase.execute(request, "teste");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> listAvailableOperations() {
        log.info("[Operation] Listing available operation types");
        return ResponseEntity.ok(OperationType.values());
    }
}
