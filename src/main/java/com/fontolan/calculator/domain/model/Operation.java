package com.fontolan.calculator.domain.model;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;

public class Operation {
    private Long id;
    private final OperationType type;
    private final BigDecimal cost;

    public Operation(Long id, OperationType type, BigDecimal cost) {
        this.id = id;
        this.type = type;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationType getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }
}