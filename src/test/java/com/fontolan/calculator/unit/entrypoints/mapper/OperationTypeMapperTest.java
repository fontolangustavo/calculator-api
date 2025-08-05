package com.fontolan.calculator.unit.entrypoints.mapper;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.mapper.OperationTypeMapper;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OperationTypeMapperTest {

    private OperationTypeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OperationTypeMapper();
    }

    @Test
    void shouldMapDomainToResponse() {
        Operation operation = mockOperation(123L);

        OperationTypeResponse response = mapper.toResponse(operation);

        assertThat(response.getType()).isEqualTo(OperationType.ADDITION);
        assertThat(response.getCost()).isEqualTo(new BigDecimal("1.5"));
    }

    @Test
    void shouldMapListOfDomainToListOfResponse() {
        List<Operation> operations = List.of(mockOperation(123L), mockOperation(321L));

        List<OperationTypeResponse> response = operations.stream().map(mapper::toResponse)
                .toList();

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getType()).isEqualTo(OperationType.ADDITION);
        assertThat(response.get(0).getCost()).isEqualTo(new BigDecimal("1.5"));
        assertThat(response.get(1).getType()).isEqualTo(OperationType.ADDITION);
        assertThat(response.get(1).getCost()).isEqualTo(new BigDecimal("1.5"));
    }

    public Operation mockOperation(Long id) {
        OperationType operationType = OperationType.ADDITION;
        BigDecimal cost = new BigDecimal("1.5");

        return new Operation(id, operationType, cost);
    }
}