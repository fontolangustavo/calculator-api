package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.entrypoints.controllers.OperationController;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class OperationControllerImpl implements OperationController {

    private static final Logger log = getLogger(OperationControllerImpl.class);

    @Override
    public ResponseEntity<OperationResponse> executeOperation(OperationRequest request) {
        log.info("[Operation] Executing operation of type: {} with operands: {}", request.getType(), request.getOperands());

        OperationResponse response = new OperationResponse();
        response.setType(request.getType());
        response.setInput(request.getOperands().toString());
        response.setResult("mocked-result");
        response.setUserBalance(BigDecimal.valueOf(99.50));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> listAvailableOperations() {
        log.info("[Operation] Listing available operation types");
        return ResponseEntity.ok(OperationType.values());
    }
}
