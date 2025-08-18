package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.AbstractOperationHandler;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractOperationHandlerTest {

    static class TestHandler extends AbstractOperationHandler {
        void callRequireAtLeast(int n, List<BigDecimal> operands) {
            requireAtLeast(n, operands);
        }

        void callRequireExactly(int n, List<BigDecimal> operands) {
            requireExactly(n, operands);
        }
    }

    private final TestHandler handler = new TestHandler();

    @Test
    void requireAtLeast_shouldThrow_whenOperandsIsNull() {
        int n = 2;
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> handler.callRequireAtLeast(n, null)
        );
        assertEquals("Requires at least 2 operands", ex.getMessage());
    }

    @Test
    void requireAtLeast_shouldThrow_whenSizeLessThanN() {
        int n = 3;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> handler.callRequireAtLeast(n, ops)
        );
        assertEquals("Requires at least 3 operands", ex.getMessage());
    }

    @Test
    void requireAtLeast_shouldPass_whenSizeEqualsN() {
        int n = 2;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN);
        assertDoesNotThrow(() -> handler.callRequireAtLeast(n, ops));
    }

    @Test
    void requireAtLeast_shouldPass_whenSizeGreaterThanN() {
        int n = 2;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO);
        assertDoesNotThrow(() -> handler.callRequireAtLeast(n, ops));
    }

    @Test
    void requireExactly_shouldThrow_whenOperandsIsNull() {
        int n = 3;
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> handler.callRequireExactly(n, null)
        );
        assertEquals("Requires exactly 3 operands", ex.getMessage());
    }

    @Test
    void requireExactly_shouldThrow_whenSizeNotEqualToN_less() {
        int n = 3;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> handler.callRequireExactly(n, ops)
        );
        assertEquals("Requires exactly 3 operands", ex.getMessage());
    }

    @Test
    void requireExactly_shouldThrow_whenSizeNotEqualToN_greater() {
        int n = 2;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> handler.callRequireExactly(n, ops)
        );
        assertEquals("Requires exactly 2 operands", ex.getMessage());
    }

    @Test
    void requireExactly_shouldPass_whenSizeEqualsN() {
        int n = 2;
        List<BigDecimal> ops = List.of(BigDecimal.ONE, BigDecimal.TEN);
        assertDoesNotThrow(() -> handler.callRequireExactly(n, ops));
    }
}
