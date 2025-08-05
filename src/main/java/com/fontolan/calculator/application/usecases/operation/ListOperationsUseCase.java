package com.fontolan.calculator.application.usecases.operation;

import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import org.springframework.data.domain.Page;

public interface ListOperationsUseCase {
    Page<Operation> execute(OperationFilterRequest request);
}
