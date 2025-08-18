package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.DivisionOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

class DivisionOperationHandlerTest {

    private DivisionOperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DivisionOperationHandler();
    }

    @Test
    void shouldDivideTwoPositiveOperands() {
        var operands = List.of(new BigDecimal("10"), new BigDecimal("4"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("2.5");
    }

    @Test
    void shouldHandleZeroNumerator() {
        var operands = List.of(new BigDecimal("0"), new BigDecimal("5"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("0");
    }

    @Test
    void shouldKeepPlainStringWithoutScientificNotation() {
        var operands = List.of(new BigDecimal("1000000000000000000"), new BigDecimal("2"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("500000000000000000");
    }

    @Test
    void shouldDivideWithHighPrecisionAndStripTrailingZeros_oneThird() {
        var operands = List.of(new BigDecimal("1"), new BigDecimal("3"));

        String result = handler.handle(operands);

        assertThat(result).startsWith("0.3333333333333333");
        assertThat(result).doesNotContain("E");
    }

    @Test
    void shouldDivideWithHighPrecision_twoThirds() {
        var operands = List.of(new BigDecimal("2"), new BigDecimal("3"));

        String result = handler.handle(operands);

        assertThat(result).startsWith("0.6666666666666666");
        assertThat(result).doesNotContain("E");
    }

    @Test
    void shouldHandleNegativeNumbers() {
        var operands = List.of(new BigDecimal("-10"), new BigDecimal("4"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("-2.5");
    }

    @Test
    void shouldThrowWhenDividingByZero() {
        var operands = List.of(new BigDecimal("10"), BigDecimal.ZERO);

        assertThatThrownBy(() -> handler.handle(operands))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("Division by zero")
                .hasMessageContaining("non-zero divisor");
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = Mockito.spy(new DivisionOperationHandler());
        var operands = List.of(new BigDecimal("10"), new BigDecimal("2"));

        spy.handle(operands);

        verify(spy).validateOperands(operands);
    }

    @Test
    void validateOperands_shouldThrowWhenNull() {
        assertThatThrownBy(() -> handler.validateOperands(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 2");
    }

    @Test
    void validateOperands_shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> handler.validateOperands(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 2");
    }

    @Test
    void validateOperands_shouldThrowWhenSingleOperand() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("1"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 2");
    }

    @Test
    void validateOperands_shouldThrowWhenMoreThanTwoOperands() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(
                new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3")))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 2");
    }

    @Test
    void validateOperands_shouldPassWhenExactlyTwoOperands() {
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"))))
                .doesNotThrowAnyException();
    }
}
