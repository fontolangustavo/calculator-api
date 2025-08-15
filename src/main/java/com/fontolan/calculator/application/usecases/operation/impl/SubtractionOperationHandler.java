package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SubtractionOperationHandler extends AbstractOperationHandler implements OperationHandler {
    @Override
    public String handle(List<BigDecimal> operands) {
        validateOperands(operands);

        BigDecimal result = operands.get(0);
        for (int i = 1; i < operands.size(); i++) {
            result = result.subtract(operands.get(i));
        }

        return result.toPlainString();
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) {
        requireExactly(2, operands);
    }
}
