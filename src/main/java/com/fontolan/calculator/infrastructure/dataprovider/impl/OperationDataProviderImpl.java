package com.fontolan.calculator.infrastructure.dataprovider.impl;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.repository.OperationRepository;
import com.fontolan.calculator.infrastructure.mapper.OperationEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class OperationDataProviderImpl implements OperationDataProvider {
    private final OperationRepository operationRepository;
    private final OperationEntityMapper operationEntityMapper;

    public OperationDataProviderImpl(OperationRepository operationRepository, OperationEntityMapper operationEntityMapper) {
        this.operationRepository = operationRepository;
        this.operationEntityMapper = operationEntityMapper;
    }

    @Override
    public Operation findByType(OperationType type) {
        return operationRepository.findByType(type)
                .map(operationEntityMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Operation not found for type: " + type));
    }
}
