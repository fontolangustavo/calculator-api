package com.fontolan.calculator.entrypoints.request;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;
import java.util.List;

public class OperationRequest {
    private OperationType type;
    private List<BigDecimal> operands;

    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }
    public List<BigDecimal> getOperands() { return operands; }
    public void setOperands(List<BigDecimal> operands) { this.operands = operands; }
}
