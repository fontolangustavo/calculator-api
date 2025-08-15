package com.fontolan.calculator.application.usecases.operation.impl;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractOperationHandler {

    protected void requireAtLeast(int n, List<BigDecimal> operands) {
        if (operands == null || operands.size() < n)
            throw new IllegalArgumentException("Requires at least " + n + " operands");
    }

    protected void requireExactly(int n, List<BigDecimal> operands) {
        if (operands == null || operands.size() != n)
            throw new IllegalArgumentException("Requires exactly " + n + " operands");
    }
}
