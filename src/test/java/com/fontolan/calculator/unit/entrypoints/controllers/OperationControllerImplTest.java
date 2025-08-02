package com.fontolan.calculator.unit.entrypoints.controllers;

import com.fontolan.calculator.application.usecases.operation.PerformOperationUseCase;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.entrypoints.controllers.impl.OperationControllerImpl;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
}
