package com.fontolan.calculator.unit.infrastructure.mapper;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import com.fontolan.calculator.infrastructure.mapper.RecordEntityMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecordEntityMapperTest {
    private final RecordEntityMapper mapper = new RecordEntityMapper();

    @Test
    void shouldConvertToEntity() {
        Record record = new Record(UUID.randomUUID(), UUID.randomUUID(),OperationType.ADDITION, "7 + 2", BigDecimal.TEN, "9", LocalDateTime.now(), null);

        RecordEntity entity = mapper.toEntity(record);

        assertNotNull(entity);
        assertEquals(record.getId(), entity.getId());
        assertEquals(record.getUserId(), entity.getUserId());
        assertEquals(record.getOperationType(), entity.getOperationType());
        assertEquals(record.getAmount(), entity.getAmount());
        assertEquals(record.getUserBalance(), entity.getUserBalance());
        assertEquals(record.getOperationResponse(), entity.getOperationResponse());
        assertEquals(record.getCreatedAt(), entity.getCreatedAt());
    }
}

