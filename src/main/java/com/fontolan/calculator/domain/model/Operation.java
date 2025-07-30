package com.fontolan.calculator.domain.model;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;

public class Operation {
    private final OperationType type;
    private final BigDecimal cost;

    public Operation(OperationType type, BigDecimal cost) {
        this.type = type;
        this.cost = cost;
    }

    public OperationType getType() { return type; }
    public BigDecimal getCost() { return cost; }
}