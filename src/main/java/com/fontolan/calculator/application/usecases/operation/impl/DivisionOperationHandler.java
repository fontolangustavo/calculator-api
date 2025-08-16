package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Component
public class DivisionOperationHandler extends AbstractOperationHandler implements OperationHandler {
    @Override
    public String handle(List<BigDecimal> operands) {
        validateOperands(operands);

        BigDecimal result = operands.get(0);
        for (int i = 1; i < operands.size(); i++) {
            if (operands.get(i).compareTo(BigDecimal.ZERO) == 0) {
                throw new ArithmeticException("Division by zero is not allowed. Please provide a non-zero divisor.");
            }
            result = result.divide(operands.get(i), MathContext.DECIMAL128);
        }
        return result.stripTrailingZeros().toPlainString();
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) {
        requireExactly(2, operands);
    }
}
