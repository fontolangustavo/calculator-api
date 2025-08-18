package com.fontolan.calculator.unit.application.usecases;


import com.fontolan.calculator.application.usecases.operation.impl.SquareRootOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

class SquareRootOperationHandlerTest {

    private SquareRootOperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SquareRootOperationHandler();
    }

    @Test
    void shouldComputeSquareRootOfPerfectSquare() {
        var result = handler.handle(List.of(new BigDecimal("49")));
        assertThat(result).isEqualTo("7");
    }

    @Test
    void shouldComputeSquareRootOfNonPerfectSquare() {
        var result = handler.handle(List.of(new BigDecimal("2")));
        assertThat(result).isEqualTo("1.4142135623730951");
        assertThat(result).doesNotContain("E");
    }

    @Test
    void shouldComputeSquareRootOfZero() {
        var result = handler.handle(List.of(new BigDecimal("0")));
        assertThat(result).isEqualTo("0");
    }

    @Test
    void shouldReturnPlainStringWithoutScientificNotationForLargeNumber() {
        var result = handler.handle(List.of(new BigDecimal("1000000000000000000")));
        assertThat(result).isEqualTo("1000000000");
        assertThat(result).doesNotContain("E");
    }

    @Test
    void shouldThrowWhenNegativeInput() {
        assertThatThrownBy(() -> handler.handle(List.of(new BigDecimal("-1"))))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("Square root is not defined for negative numbers")
                .hasMessageContaining("-1");
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = Mockito.spy(new SquareRootOperationHandler());
        spy.handle(List.of(new BigDecimal("16")));
        verify(spy).validateOperands(List.of(new BigDecimal("16")));
    }

    @Test
    void validateOperands_shouldThrowWhenNull() {
        assertThatThrownBy(() -> handler.validateOperands(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
    }

    @Test
    void validateOperands_shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> handler.validateOperands(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
    }

    @Test
    void validateOperands_shouldThrowWhenMoreThanOne() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
    }

    @Test
    void validateOperands_shouldPassWhenExactlyOne() {
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("9"))))
                .doesNotThrowAnyException();
    }
}
