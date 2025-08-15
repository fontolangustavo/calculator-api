package com.fontolan.calculator.application.usecases.operation.impl;

import com.fontolan.calculator.application.usecases.operation.OperationHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RandomStringOperationHandler extends AbstractOperationHandler implements OperationHandler {

    @Override
    public String handle(List<BigDecimal> operands) {
        int length = operands != null && !operands.isEmpty()
                ? operands.get(0).intValue()
                : 10;

        return generateRandomString(length);
    }

    @Override
    public void validateOperands(List<BigDecimal> operands) {
        if (operands != null && !operands.isEmpty()) {
            throw new IllegalArgumentException("RANDOM_STRING doesn't accept operands");
        }
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}
