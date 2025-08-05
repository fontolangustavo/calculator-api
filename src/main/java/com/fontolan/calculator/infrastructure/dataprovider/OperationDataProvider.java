package com.fontolan.calculator.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import org.springframework.data.domain.Page;

public interface OperationDataProvider {
    Operation findByType(OperationType type);
    Page<Operation> findAll(OperationFilterRequest request);
}
