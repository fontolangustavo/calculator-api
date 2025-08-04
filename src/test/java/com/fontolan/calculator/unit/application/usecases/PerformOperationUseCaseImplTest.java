package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.PerformOperationUseCaseImpl;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformOperationUseCaseImplTest {

    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private OperationDataProvider operationDataProvider;

    @Mock
    private RecordDataProvider recordDataProvider;

    @InjectMocks
    private PerformOperationUseCaseImpl useCase;

    @Test
    void shouldPerformAdditionAndSaveRecord() {
        UUID userId = UUID.randomUUID();
        List<BigDecimal> operands = List.of(BigDecimal.valueOf(2), BigDecimal.valueOf(3));
        OperationType type = OperationType.ADDITION;

        OperationRequest request = new OperationRequest(type, operands);

        User user = new User(userId, "john", "pass", UserStatus.ACTIVE, BigDecimal.valueOf(10));
        Operation operation = new Operation(1L, type, BigDecimal.valueOf(0.01));

        when(userDataProvider.findByUsername(anyString())).thenReturn(user);
        when(operationDataProvider.findByType(type)).thenReturn(operation);

        ArgumentCaptor<Record> recordCaptor = ArgumentCaptor.forClass(Record.class);

        OperationResponse response = useCase.execute(request, "john");

        assertEquals(type, response.getType());
        assertEquals(List.of(BigDecimal.valueOf(2), BigDecimal.valueOf(3)), response.getOperands());
        assertEquals("5", response.getResult());
        assertEquals(BigDecimal.valueOf(9.99), response.getUserBalance());

        verify(recordDataProvider).save(recordCaptor.capture());

        Record savedRecord = recordCaptor.getValue();
        assertEquals(userId, savedRecord.getUserId());
        assertEquals(type, savedRecord.getOperationType());
        assertEquals("5", savedRecord.getOperationResponse());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        OperationRequest request = new OperationRequest(OperationType.ADDITION, List.of(BigDecimal.ONE, BigDecimal.ONE));

        when(userDataProvider.findByUsername(anyString())).thenThrow(new NotFoundException("User not found: MOCK_NAME"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            useCase.execute(request, "MOCK_NAME");
        });

        assertEquals("User not found: MOCK_NAME", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOperationNotFound() {
        UUID userId = UUID.randomUUID();
        OperationType type = OperationType.SUBTRACTION;
        OperationRequest request = new OperationRequest(type, List.of(BigDecimal.TEN, BigDecimal.ONE));
        User user = new User(userId, "MOCK_NAME", "pass", UserStatus.ACTIVE, BigDecimal.TEN);

        when(userDataProvider.findByUsername(anyString())).thenReturn(user);
        when(operationDataProvider.findByType(type)).thenThrow(new NotFoundException("Operation not found for type: " + type));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            useCase.execute(request, "MOCK_NAME");
        });

        assertEquals("Operation not found for type: " + type, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        UUID userId = UUID.randomUUID();
        OperationType type = OperationType.DIVISION;
        OperationRequest request = new OperationRequest(type, List.of(BigDecimal.TEN, BigDecimal.TEN));
        User user = new User(userId, "john", "pass", UserStatus.ACTIVE, BigDecimal.valueOf(0.01));
        Operation operation = new Operation(3L, type, BigDecimal.valueOf(0.02));

        when(userDataProvider.findByUsername(anyString())).thenReturn(user);
        when(operationDataProvider.findByType(type)).thenReturn(operation);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            useCase.execute(request, user.getUsername());
        });

        assertEquals("Insufficient balance for operation", exception.getMessage());
    }
}

