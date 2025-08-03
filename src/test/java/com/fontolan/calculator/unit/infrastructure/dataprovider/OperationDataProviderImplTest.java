package com.fontolan.calculator.unit.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import com.fontolan.calculator.infrastructure.dataprovider.impl.OperationDataProviderImpl;
import com.fontolan.calculator.infrastructure.dataprovider.repository.OperationRepository;
import com.fontolan.calculator.infrastructure.mapper.OperationEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OperationDataProviderImplTest {

    private OperationRepository operationRepository;
    private OperationEntityMapper operationEntityMapper;
    private OperationDataProviderImpl dataProvider;

    @BeforeEach
    void setUp() {
        operationRepository = mock(OperationRepository.class);
        operationEntityMapper = mock(OperationEntityMapper.class);
        dataProvider = new OperationDataProviderImpl(operationRepository, operationEntityMapper);
    }

    @Test
    void shouldReturnOperationWhenTypeExists() {
        OperationType type = OperationType.ADDITION;
        OperationEntity entity = new OperationEntity();
        Operation operation = new Operation(123L, OperationType.MULTIPLICATION, BigDecimal.ONE);

        when(operationRepository.findByType(type)).thenReturn(Optional.of(entity));
        when(operationEntityMapper.toDomain(entity)).thenReturn(operation);

        Operation result = dataProvider.findByType(type);

        assertThat(result).isEqualTo(operation);
        verify(operationRepository).findByType(type);
        verify(operationEntityMapper).toDomain(entity);
    }

    @Test
    void shouldThrowExceptionWhenOperationNotFound() {
        OperationType type = OperationType.SUBTRACTION;

        when(operationRepository.findByType(type)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dataProvider.findByType(type);
        });

        assertThat(exception.getMessage()).isEqualTo("Operation not found for type: " + type);
    }
}
