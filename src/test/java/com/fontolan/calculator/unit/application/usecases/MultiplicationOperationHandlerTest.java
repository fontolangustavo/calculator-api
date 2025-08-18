package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.MultiplicationOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

class MultiplicationOperationHandlerTest {

    private MultiplicationOperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new MultiplicationOperationHandler();
    }

    @Test
    void shouldMultiplyTwoOperands() {
        var operands = List.of(new BigDecimal("3"), new BigDecimal("4"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("12");
    }

    @Test
    void shouldMultiplyMultipleOperands() {
        var operands = List.of(new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("4"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("24");
    }

    @Test
    void shouldHandleZeroOperand() {
        var operands = List.of(new BigDecimal("0"), new BigDecimal("999"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("0");
    }

    @Test
    void shouldHandleNegativeNumbers() {
        var operands = List.of(new BigDecimal("-2"), new BigDecimal("3"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("-6");
    }

    @Test
    void shouldKeepDecimalPrecision() {
        var operands = List.of(new BigDecimal("1.5"), new BigDecimal("0.2"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("0.30");
    }

    @Test
    void shouldReturnPlainStringWithoutScientificNotation() {
        var operands = List.of(new BigDecimal("1000000000000000000"), new BigDecimal("1000"));
        String result = handler.handle(operands);
        assertThat(result).isEqualTo("1000000000000000000000");
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = Mockito.spy(new MultiplicationOperationHandler());
        var operands = List.of(new BigDecimal("2"), new BigDecimal("3"));
        spy.handle(operands);
        verify(spy).validateOperands(operands);
    }

    @Test
    void validateOperands_shouldThrowWhenNull() {
        assertThatThrownBy(() -> handler.validateOperands(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 2");
    }

    @Test
    void validateOperands_shouldThrowWhenEmpty() {
        assertThatThrownBy(() -> handler.validateOperands(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 2");
    }

    @Test
    void validateOperands_shouldThrowWhenSingleOperand() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("1"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 2");
    }

    @Test
    void validateOperands_shouldPassWhenTwoOrMoreOperands() {
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"))))
                .doesNotThrowAnyException();
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"))))
                .doesNotThrowAnyException();
    }
}
