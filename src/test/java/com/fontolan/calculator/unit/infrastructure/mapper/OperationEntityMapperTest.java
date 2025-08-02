package com.fontolan.calculator.unit.infrastructure.mapper;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import com.fontolan.calculator.infrastructure.mapper.OperationEntityMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OperationEntityMapperTest {
    private final OperationEntityMapper mapper = new OperationEntityMapper();

    @Test
    void shouldConvertToDomain() {
        OperationEntity entity = new OperationEntity();
        entity.setId(1L);
        entity.setType(OperationType.DIVISION);
        entity.setCost(BigDecimal.valueOf(0.02));

        Operation domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getType(), domain.getType());
        assertEquals(entity.getCost(), domain.getCost());
    }
}