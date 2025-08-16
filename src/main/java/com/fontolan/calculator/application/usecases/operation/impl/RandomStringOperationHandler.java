package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import com.fontolan.calculator.infrastructure.dataprovider.RandomStringDataProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RandomStringOperationHandler extends AbstractOperationHandler implements OperationHandler {
    private final RandomStringDataProvider randomStringDataProvider;

    public RandomStringOperationHandler(RandomStringDataProvider randomStringDataProvider) {
        this.randomStringDataProvider = randomStringDataProvider;
    }

    @Override
    public String handle(List<BigDecimal> operands) {
        int length = operands != null && !operands.isEmpty()
                ? operands.get(0).intValue()
                : 10;

        return randomStringDataProvider.getRandomString(length);
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) { }
}
