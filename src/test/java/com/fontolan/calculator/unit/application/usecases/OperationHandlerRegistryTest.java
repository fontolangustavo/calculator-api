package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.AdditionOperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.DivisionOperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.MultiplicationOperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.OperationHandlerRegistry;
import com.fontolan.calculator.application.usecases.operation.impl.RandomStringOperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.SquareRootOperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.SubtractionOperationHandler;
import com.fontolan.calculator.domain.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class OperationHandlerRegistryTest {

    private AdditionOperationHandler additionHandler;
    private SubtractionOperationHandler subtractionHandler;
    private MultiplicationOperationHandler multiplicationHandler;
    private DivisionOperationHandler divisionHandler;
    private SquareRootOperationHandler squareRootHandler;
    private RandomStringOperationHandler randomStringHandler;
    private OperationHandlerRegistry registry;

    @BeforeEach
    void setUp() {
        additionHandler = mock(AdditionOperationHandler.class);
        subtractionHandler = mock(SubtractionOperationHandler.class);
        multiplicationHandler = mock(MultiplicationOperationHandler.class);
        divisionHandler = mock(DivisionOperationHandler.class);
        squareRootHandler = mock(SquareRootOperationHandler.class);
        randomStringHandler = mock(RandomStringOperationHandler.class);

        registry = new OperationHandlerRegistry(
                additionHandler,
                subtractionHandler,
                multiplicationHandler,
                divisionHandler,
                squareRootHandler,
                randomStringHandler
        );
    }

    @Test
    void shouldReturnAdditionHandler() {
        OperationHandler result = registry.getHandler(OperationType.ADDITION);
        assertThat(result).isSameAs(additionHandler);
    }

    @Test
    void shouldReturnSubtractionHandler() {
        OperationHandler result = registry.getHandler(OperationType.SUBTRACTION);
        assertThat(result).isSameAs(subtractionHandler);
    }

    @Test
    void shouldReturnMultiplicationHandler() {
        OperationHandler result = registry.getHandler(OperationType.MULTIPLICATION);
        assertThat(result).isSameAs(multiplicationHandler);
    }

    @Test
    void shouldReturnDivisionHandler() {
        OperationHandler result = registry.getHandler(OperationType.DIVISION);
        assertThat(result).isSameAs(divisionHandler);
    }

    @Test
    void shouldReturnSquareRootHandler() {
        OperationHandler result = registry.getHandler(OperationType.SQUARE_ROOT);
        assertThat(result).isSameAs(squareRootHandler);
    }

    @Test
    void shouldReturnRandomStringHandler() {
        OperationHandler result = registry.getHandler(OperationType.RANDOM_STRING);
        assertThat(result).isSameAs(randomStringHandler);
    }

    @Test
    void shouldThrowWhenOperationTypeNotRegistered() {
        assertThatThrownBy(() -> registry.getHandler(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No handler found for operation type");
    }
}
