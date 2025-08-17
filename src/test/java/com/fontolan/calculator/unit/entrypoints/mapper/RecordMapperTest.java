package com.fontolan.calculator.unit.entrypoints.mapper;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.mapper.RecordMapper;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecordMapperTest {

    private RecordMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecordMapper();
    }

    @Test
    void shouldMapRecordToRecordResponse() {
        Record record = new Record(
                UUID.randomUUID(),
                UUID.randomUUID(),
                OperationType.ADDITION,
                "100",
                BigDecimal.valueOf(500),
                "100 + 50 = 150",
                Instant.now(),
                null
        );

        RecordResponse response = mapper.toResponse(record);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(record.getId());
        assertThat(response.getOperationType()).isEqualTo(record.getOperationType());
        assertThat(response.getAmount()).isEqualTo(record.getAmount());
        assertThat(response.getUserBalance()).isEqualTo(record.getUserBalance());
        assertThat(response.getOperationResponse()).isEqualTo(record.getOperationResponse());
        assertThat(response.getCreatedAt()).isEqualTo(record.getCreatedAt());
    }
}