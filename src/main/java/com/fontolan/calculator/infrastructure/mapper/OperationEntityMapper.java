package com.fontolan.calculator.infrastructure.mapper;

import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import org.springframework.stereotype.Component;

@Component
public class OperationEntityMapper {
    public Operation toDomain(OperationEntity entity) {
        return new Operation(
                entity.getId(),
                entity.getType(),
                entity.getCost()
        );
    }
}
