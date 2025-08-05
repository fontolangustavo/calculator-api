package com.fontolan.calculator.infrastructure.dataprovider.impl;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.repository.OperationRepository;
import com.fontolan.calculator.infrastructure.mapper.OperationEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new NotFoundException("Operation not found for type: " + type));
    }

    @Override
    public Page<Operation> findAll(OperationFilterRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                        Sort.Direction.valueOf(request.getSortDir().toUpperCase()),
                        request.getSortBy()
                )
        );

        return operationRepository.findAll(pageable)
                .map(operationEntityMapper::toDomain);
    }
}
