package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.ListOperationsUseCaseImpl;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.OperationDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ListOperationsUseCaseImplTest {

    private OperationDataProvider operationDataProvider;
    private ListOperationsUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        operationDataProvider = mock(OperationDataProvider.class);
        useCase = new ListOperationsUseCaseImpl(operationDataProvider);
    }

    @Test
    void shouldReturnOperationsFromDataProvider() {
        OperationFilterRequest request = new OperationFilterRequest();

        Operation op1 = mock(Operation.class);
        Operation op2 = mock(Operation.class);
        Page<Operation> expected = new PageImpl<>(List.of(op1, op2));

        when(operationDataProvider.findAll(request)).thenReturn(expected);

        Page<Operation> result = useCase.execute(request);

        assertThat(result).isSameAs(expected);
        verify(operationDataProvider).findAll(request);
        verifyNoMoreInteractions(operationDataProvider);
    }

    @Test
    void shouldReturnEmptyPageWhenDataProviderReturnsEmpty() {
        OperationFilterRequest request = new OperationFilterRequest();
        Page<Operation> empty = Page.empty();

        when(operationDataProvider.findAll(request)).thenReturn(empty);

        Page<Operation> result = useCase.execute(request);

        assertThat(result).isSameAs(empty);
        verify(operationDataProvider).findAll(request);
        verifyNoMoreInteractions(operationDataProvider);
    }

    @Test
    void shouldForwardNullRequestIfAllowed() {
        Page<Operation> empty = Page.empty();
        when(operationDataProvider.findAll(null)).thenReturn(empty);

        Page<Operation> result = useCase.execute(null);

        assertThat(result).isSameAs(empty);
        verify(operationDataProvider).findAll(null);
        verifyNoMoreInteractions(operationDataProvider);
    }
}