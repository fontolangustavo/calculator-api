package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AdditionOperationHandler extends AbstractOperationHandler implements OperationHandler {
    @Override
    public String handle(List<BigDecimal> operands) {
        validateOperands(operands);

        return operands.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toPlainString();
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) {
        requireAtLeast(2, operands);
    }
}
