package com.fontolan.calculator.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;

public interface OperationDataProvider {
    Operation findByType(OperationType type);
}
