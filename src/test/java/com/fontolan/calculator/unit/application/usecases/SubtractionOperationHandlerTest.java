package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.SubtractionOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

class SubtractionOperationHandlerTest {

    private SubtractionOperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SubtractionOperationHandler();
    }

    @Test
    void shouldSubtractTwoPositiveOperands() {
        var operands = List.of(new BigDecimal("10"), new BigDecimal("4"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("6");
    }

    @Test
    void shouldHandleNegativeNumbers() {
        var operands = List.of(new BigDecimal("5"), new BigDecimal("-3"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("8");
    }

    @Test
    void shouldHandleDecimals() {
        var operands = List.of(new BigDecimal("5.5"), new BigDecimal("2.25"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("3.25");
    }

    @Test
    void shouldHandleZero() {
        var operands = List.of(new BigDecimal("0"), new BigDecimal("7"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("-7");
    }

    @Test
    void shouldReturnPlainStringWithoutScientificNotation() {
        var operands = List.of(new BigDecimal("1000000000000000000"), new BigDecimal("1"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("999999999999999999");
        assertThat(result).doesNotContain("E");
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = Mockito.spy(new SubtractionOperationHandler());
        var operands = List.of(new BigDecimal("9"), new BigDecimal("3"));
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
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 2");
    }

    @Test
    void validateOperands_shouldPassWhenExactlyTwoOperands() {
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"))))
                .doesNotThrowAnyException();
    }
}
