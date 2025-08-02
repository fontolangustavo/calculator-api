package com.fontolan.calculator.domain.enums;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public enum OperationType {
    ADDITION {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            requireAtLeast(2, operands);
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            validateOperands(operands);
            return operands.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toPlainString();
        }
    },
    SUBTRACTION {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            requireExactly(2, operands);
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            validateOperands(operands);

            BigDecimal result = operands.get(0);
            for (int i = 1; i < operands.size(); i++) {
                result = result.subtract(operands.get(i));
            }

            return result.toPlainString();
        }
    },
    MULTIPLICATION {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            requireAtLeast(2, operands);
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            validateOperands(operands);

            return operands.stream().reduce(BigDecimal.ONE, BigDecimal::multiply).toPlainString();
        }
    },
    DIVISION {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            requireExactly(2, operands);
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            validateOperands(operands);

            BigDecimal result = operands.get(0);
            for (int i = 1; i < operands.size(); i++) {
                if (operands.get(i).compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = result.divide(operands.get(i), MathContext.DECIMAL128);
            }
            return result.stripTrailingZeros().toPlainString();
        }
    },
    SQUARE_ROOT {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            requireExactly(1, operands);
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            validateOperands(operands);

            BigDecimal input = operands.get(0);
            if (input.compareTo(BigDecimal.ZERO) < 0) {
                throw new ArithmeticException("Cannot compute square root of negative number");
            }

            double sqrt = Math.sqrt(input.doubleValue());

            return BigDecimal.valueOf(sqrt).stripTrailingZeros().toPlainString();
        }
    },
    RANDOM_STRING {
        @Override
        public void validateOperands(List<BigDecimal> operands) {
            if (operands != null && !operands.isEmpty()) {
                throw new IllegalArgumentException("RANDOM_STRING doesn't accept operands");
            }
        }

        @Override
        public String execute(List<BigDecimal> operands) {
            int length = operands != null && !operands.isEmpty()
                    ? operands.get(0).intValue()
                    : 10;
            return generateRandomString(length);
        }
    };

    public abstract String execute(List<BigDecimal> operands);

    public abstract void validateOperands(List<BigDecimal> operands);

    protected void requireAtLeast(int n, List<BigDecimal> operands) {
        if (operands == null || operands.size() < n)
            throw new IllegalArgumentException("Requires at least " + n + " operands");
    }

    protected void requireExactly(int n, List<BigDecimal> operands) {
        if (operands == null || operands.size() != n)
            throw new IllegalArgumentException("Requires exactly " + n + " operands");
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
