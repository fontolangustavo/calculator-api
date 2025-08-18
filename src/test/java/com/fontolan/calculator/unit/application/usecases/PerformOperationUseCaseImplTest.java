package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import com.fontolan.calculator.application.usecases.operation.impl.OperationHandlerRegistry;
import com.fontolan.calculator.application.usecases.operation.impl.PerformOperationUseCaseImpl;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PerformOperationUseCaseImplTest {

    private UserDataProvider userDataProvider;
    private OperationDataProvider operationDataProvider;
    private RecordDataProvider recordDataProvider;
    private OperationHandlerRegistry handlerRegistry;
    private OperationHandler operationHandler;
    private PerformOperationUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userDataProvider = mock(UserDataProvider.class);
        operationDataProvider = mock(OperationDataProvider.class);
        recordDataProvider = mock(RecordDataProvider.class);
        handlerRegistry = mock(OperationHandlerRegistry.class);
        operationHandler = mock(OperationHandler.class);
        useCase = new PerformOperationUseCaseImpl(userDataProvider, operationDataProvider, recordDataProvider, handlerRegistry);
    }

    @Test
    void shouldPerformOperationAndPersistRecord() {
        var username = "gustavo";
        var userId = UUID.randomUUID();
        var cost = new BigDecimal("2.50");
        var remaining = new BigDecimal("97.50");
        var type = OperationType.ADDITION;
        var operands = List.of(new BigDecimal("40"), new BigDecimal("2"));
        var request = new OperationRequest(type, operands);
        var user = mock(User.class);
        var operation = mock(Operation.class);

        when(userDataProvider.findByUsername(username)).thenReturn(user);
        when(user.getId()).thenReturn(userId);
        when(operationDataProvider.findByType(type)).thenReturn(operation);
        when(operation.getCost()).thenReturn(cost);
        doNothing().when(user).deductBalance(cost);
        when(user.getBalance()).thenReturn(remaining);
        when(handlerRegistry.getHandler(type)).thenReturn(operationHandler);
        when(operationHandler.handle(operands)).thenReturn("42");

        var response = useCase.execute(request, username);

        InOrder inOrder = inOrder(userDataProvider, operationDataProvider, handlerRegistry, user, userDataProvider, recordDataProvider);
        inOrder.verify(userDataProvider).findByUsername(username);
        inOrder.verify(operationDataProvider).findByType(type);
        inOrder.verify(user).deductBalance(cost);
        inOrder.verify(handlerRegistry).getHandler(type);
        verify(operationHandler).handle(operands);
        verify(userDataProvider).updateBalance(userId, remaining);

        ArgumentCaptor<Record> recordCaptor = ArgumentCaptor.forClass(Record.class);
        verify(recordDataProvider).save(recordCaptor.capture());
        var saved = recordCaptor.getValue();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getOperationType()).isEqualTo(type);
        assertThat(saved.getAmount()).isEqualTo(operands.toString());
        assertThat(saved.getUserBalance()).isEqualTo(remaining);
        assertThat(saved.getOperationResponse()).isEqualTo("42");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getDeletedAt()).isNull();

        assertThat(response.getType()).isEqualTo(type);
        assertThat(response.getOperands()).isEqualTo(operands);
        assertThat(response.getResult()).isEqualTo("42");
        assertThat(response.getUserBalance()).isEqualTo(remaining);
    }

    @Test
    void shouldPassOperandsToHandler() {
        var username = "gustavo";
        var operands = List.of(new BigDecimal("1"), new BigDecimal("2"));
        var type = OperationType.SUBTRACTION;
        var request = new OperationRequest(type, operands);
        var user = mock(User.class);
        var operation = mock(Operation.class);

        when(userDataProvider.findByUsername(username)).thenReturn(user);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(operationDataProvider.findByType(type)).thenReturn(operation);
        when(operation.getCost()).thenReturn(new BigDecimal("1.00"));
        when(user.getBalance()).thenReturn(new BigDecimal("9.00"));
        when(handlerRegistry.getHandler(type)).thenReturn(operationHandler);
        when(operationHandler.handle(operands)).thenReturn("-1");

        useCase.execute(request, username);

        verify(operationHandler).handle(operands);
    }
}
