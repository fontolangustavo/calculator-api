package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SquareRootOperationHandler extends AbstractOperationHandler implements OperationHandler {
    @Override
    public String handle(List<BigDecimal> operands) {
        validateOperands(operands);

        BigDecimal input = operands.get(0);
        if (input.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException(
                    String.format("Square root is not defined for negative numbers. Provided value: %s", input)
            );
        }

        double sqrt = Math.sqrt(input.doubleValue());

        return BigDecimal.valueOf(sqrt).stripTrailingZeros().toPlainString();
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) {
        requireExactly(1, operands);
    }
}
