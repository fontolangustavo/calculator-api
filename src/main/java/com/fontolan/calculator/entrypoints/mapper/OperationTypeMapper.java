package com.fontolan.calculator.entrypoints.mapper;

import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class OperationTypeMapper {

    public OperationTypeResponse toResponse(Operation operation) {
        OperationTypeResponse response = new OperationTypeResponse();
        response.setId(operation.getId());
        response.setType(operation.getType());
        response.setCost(operation.getCost());

        return response;
    }
}
