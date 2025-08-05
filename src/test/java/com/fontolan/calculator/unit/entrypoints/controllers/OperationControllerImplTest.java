package com.fontolan.calculator.unit.entrypoints.controllers;

import com.fontolan.calculator.application.usecases.operation.ListOperationsUseCase;
import com.fontolan.calculator.application.usecases.operation.PerformOperationUseCase;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.controllers.impl.OperationControllerImpl;
import com.fontolan.calculator.entrypoints.mapper.OperationTypeMapper;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OperationControllerImplTest {

    @Mock
    private PerformOperationUseCase performOperationUseCase;
    @Mock
    private ListOperationsUseCase listOperationsUseCase;
    @Mock
    private OperationTypeMapper operationTypeMapper;

    @InjectMocks
    private OperationControllerImpl controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPerformOperation_success() {
        OperationRequest request = new OperationRequest(OperationType.ADDITION, List.of(BigDecimal.ONE, BigDecimal.TEN));

        OperationResponse mockedResponse = new OperationResponse(
                OperationType.ADDITION,
                List.of(BigDecimal.ONE, BigDecimal.TEN),
                "11.00",
                new BigDecimal("9.99")
        );

        when(performOperationUseCase.execute(any(OperationRequest.class), anyString())).thenReturn(mockedResponse);

        ResponseEntity<OperationResponse> response = controller.executeOperation(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockedResponse, response.getBody());

        verify(performOperationUseCase, times(1)).execute(any(OperationRequest.class), anyString());
    }

    @Test
    void shouldReturnPagedOperationTypeResponses() {
        OperationFilterRequest request = new OperationFilterRequest();
        request.setPage(0);
        request.setSize(10);

        Operation operation1 = new Operation(123L, OperationType.ADDITION, BigDecimal.ONE);
        Operation operation2 = new Operation(321L, OperationType.MULTIPLICATION, BigDecimal.TEN);

        Page<Operation> operationPage = new PageImpl<>(List.of(operation1, operation2));

        OperationTypeResponse response1 = new OperationTypeResponse(123L, OperationType.ADDITION, BigDecimal.ONE);
        OperationTypeResponse response2 = new OperationTypeResponse(321L, OperationType.MULTIPLICATION, BigDecimal.TEN);

        when(listOperationsUseCase.execute(request)).thenReturn(operationPage);
        when(operationTypeMapper.toResponse(operation1)).thenReturn(response1);
        when(operationTypeMapper.toResponse(operation2)).thenReturn(response2);

        ResponseEntity<Page<OperationTypeResponse>> response = controller.listAvailableOperations(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals(OperationType.ADDITION, response.getBody().getContent().get(0).getType());
        assertEquals(OperationType.MULTIPLICATION, response.getBody().getContent().get(1).getType());

        verify(listOperationsUseCase).execute(request);
        verify(operationTypeMapper).toResponse(operation1);
        verify(operationTypeMapper).toResponse(operation2);
    }
}
