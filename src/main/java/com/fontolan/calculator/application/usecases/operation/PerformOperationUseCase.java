package com.fontolan.calculator.application.usecases.operation;

import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;

public interface PerformOperationUseCase {
    OperationResponse execute(OperationRequest request, String username);
}
