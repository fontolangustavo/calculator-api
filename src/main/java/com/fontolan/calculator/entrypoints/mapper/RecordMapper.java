package com.fontolan.calculator.entrypoints.mapper;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {

    public RecordResponse toResponse(Record record) {
        RecordResponse response = new RecordResponse();
        response.setId(record.getId());
        response.setOperationType(record.getOperationType());
        response.setAmount(record.getAmount());
        response.setUserBalance(record.getUserBalance());
        response.setOperationResponse(record.getOperationResponse());
        response.setCreatedAt(record.getCreatedAt());

        return response;
    }
}
