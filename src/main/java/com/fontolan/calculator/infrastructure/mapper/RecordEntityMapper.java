package com.fontolan.calculator.infrastructure.mapper;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import org.springframework.stereotype.Component;

@Component
public class RecordEntityMapper {

    public RecordEntity toEntity(Record record) {
        RecordEntity entity = new RecordEntity();
        entity.setId(record.getId());
        entity.setUserId(record.getUserId());
        entity.setOperationType(record.getOperationType());
        entity.setAmount(record.getAmount());
        entity.setUserBalance(record.getUserBalance());
        entity.setOperationResponse(record.getOperationResponse());
        entity.setCreatedAt(record.getCreatedAt());
        entity.setDeletedAt(record.getDeletedAt());

        return entity;
    }
}
