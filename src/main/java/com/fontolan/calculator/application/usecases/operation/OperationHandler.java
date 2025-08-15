package com.fontolan.calculator.application.usecases.operation;

import java.math.BigDecimal;
import java.util.List;

public interface OperationHandler {
    String handle(List<BigDecimal> operands);
    void validateOperands(List<BigDecimal> operands);
}
