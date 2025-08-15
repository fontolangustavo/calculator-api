package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import com.fontolan.calculator.domain.enums.OperationType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OperationHandlerRegistry {

    private final Map<OperationType, OperationHandler> registry = new EnumMap<>(OperationType.class);

    public OperationHandlerRegistry(
            AdditionOperationHandler additionHandler,
            SubtractionOperationHandler subtractionHandler,
            MultiplicationOperationHandler multiplicationHandler,
            DivisionOperationHandler divisionHandler,
            SquareRootOperationHandler squareRootHandler,
            RandomStringOperationHandler randomStringHandler
    ) {
        registry.put(OperationType.ADDITION, additionHandler);
        registry.put(OperationType.SUBTRACTION, subtractionHandler);
        registry.put(OperationType.MULTIPLICATION, multiplicationHandler);
        registry.put(OperationType.DIVISION, divisionHandler);
        registry.put(OperationType.SQUARE_ROOT, squareRootHandler);
        registry.put(OperationType.RANDOM_STRING, randomStringHandler);
    }

    public OperationHandler getHandler(OperationType type) {
        var handler = registry.get(type);

        if (handler == null) {
            throw new IllegalArgumentException("No handler found for operation type: " + type);
        }

        return handler;
    }
}
