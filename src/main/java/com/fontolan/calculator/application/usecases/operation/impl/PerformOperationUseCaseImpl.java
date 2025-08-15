package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.PerformOperationUseCase;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PerformOperationUseCaseImpl implements PerformOperationUseCase {
    private final UserDataProvider userDataProvider;
    private final OperationDataProvider operationDataProvider;
    private final RecordDataProvider recordDataProvider;
    private final OperationHandlerRegistry operationHandlerRegistry;

    public PerformOperationUseCaseImpl(UserDataProvider userDataProvider, OperationDataProvider operationDataProvider, RecordDataProvider recordDataProvider, OperationHandlerRegistry operationHandlerRegistry) {
        this.userDataProvider = userDataProvider;
        this.operationDataProvider = operationDataProvider;
        this.recordDataProvider = recordDataProvider;
        this.operationHandlerRegistry = operationHandlerRegistry;
    }

    @Override
    public OperationResponse execute(OperationRequest request, String username) {
        User user = userDataProvider.findByUsername(username);

        Operation operation = operationDataProvider.findByType(request.getType());
        BigDecimal operationCost = operation.getCost();

        user.deductBalance(operationCost);

        String result = operationHandlerRegistry.getHandler(request.getType()).handle(request.getOperands());

        userDataProvider.updateBalance(user.getId(), user.getBalance());

        Record record = new Record(
                UUID.randomUUID(),
                user.getId(),
                request.getType(),
                request.getOperands().toString(),
                user.getBalance(),
                result,
                LocalDateTime.now(),
                null
        );

        recordDataProvider.save(record);

        return new OperationResponse(
                request.getType(),
                request.getOperands(),
                result,
                user.getBalance()
        );
    }
}
