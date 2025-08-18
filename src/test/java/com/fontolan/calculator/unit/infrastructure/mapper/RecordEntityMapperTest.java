package com.fontolan.calculator.unit.infrastructure.mapper;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import com.fontolan.calculator.infrastructure.mapper.RecordEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class RecordEntityMapperTest {

    private RecordEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecordEntityMapper();
    }

    @Test
    void toEntity_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OperationType type = OperationType.ADDITION;
        String amount = "[1, 2]";
        BigDecimal balance = new BigDecimal("98.15");
        String response = "3";
        Instant createdAt = Instant.parse("2025-08-18T10:15:30Z");
        Instant deletedAt = null;

        Record domain = new Record(id, userId, type, amount, balance, response, createdAt, deletedAt);

        RecordEntity entity = mapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getOperationType()).isEqualTo(type);
        assertThat(entity.getAmount()).isEqualTo(amount);
        assertThat(entity.getUserBalance()).isEqualTo(balance);
        assertThat(entity.getOperationResponse()).isEqualTo(response);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getDeletedAt()).isNull();
    }

    @Test
    void toDomain_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OperationType type = OperationType.DIVISION;
        String amount = "[10, 2]";
        BigDecimal balance = new BigDecimal("50.00");
        String response = "5";
        Instant createdAt = Instant.parse("2025-08-18T11:00:00Z");
        Instant deletedAt = Instant.parse("2025-08-18T12:00:00Z");

        RecordEntity entity = new RecordEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setOperationType(type);
        entity.setAmount(amount);
        entity.setUserBalance(balance);
        entity.setOperationResponse(response);
        entity.setCreatedAt(createdAt);
        entity.setDeletedAt(deletedAt);

        Record domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getOperationType()).isEqualTo(type);
        assertThat(domain.getAmount()).isEqualTo(amount);
        assertThat(domain.getUserBalance()).isEqualTo(balance);
        assertThat(domain.getOperationResponse()).isEqualTo(response);
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getDeletedAt()).isEqualTo(deletedAt);
    }

    @Test
    void toDomain_shouldReturnNullWhenEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void roundTrip_domainToEntityToDomain_shouldPreserveValues() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        OperationType type = OperationType.SUBTRACTION;
        String amount = "[7, 4]";
        BigDecimal balance = new BigDecimal("12.34");
        String response = "3";
        Instant createdAt = Instant.parse("2025-08-18T13:00:00Z");
        Instant deletedAt = null;

        Record original = new Record(id, userId, type, amount, balance, response, createdAt, deletedAt);

        RecordEntity entity = mapper.toEntity(original);
        Record mappedBack = mapper.toDomain(entity);

        assertThat(mappedBack.getId()).isEqualTo(id);
        assertThat(mappedBack.getUserId()).isEqualTo(userId);
        assertThat(mappedBack.getOperationType()).isEqualTo(type);
        assertThat(mappedBack.getAmount()).isEqualTo(amount);
        assertThat(mappedBack.getUserBalance()).isEqualTo(balance);
        assertThat(mappedBack.getOperationResponse()).isEqualTo(response);
        assertThat(mappedBack.getCreatedAt()).isEqualTo(createdAt);
        assertThat(mappedBack.getDeletedAt()).isEqualTo(deletedAt);
    }
}
