package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.ListOperationsUseCase;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ListOperationsUseCaseImpl implements ListOperationsUseCase {
    private final OperationDataProvider operationDataProvider;

    public ListOperationsUseCaseImpl(OperationDataProvider operationDataProvider) {
        this.operationDataProvider = operationDataProvider;
    }

    @Override
    public Page<Operation> execute(OperationFilterRequest request) {
        return operationDataProvider.findAll(request);
    }
}
