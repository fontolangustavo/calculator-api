package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.operation.impl.AdditionOperationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

class AdditionOperationHandlerTest {

    private AdditionOperationHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AdditionOperationHandler();
    }

    @Test
    void shouldSumTwoOperands() {
        var operands = List.of(new BigDecimal("10"), new BigDecimal("15"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("25");
    }

    @Test
    void shouldSumMultipleOperands() {
        var operands = List.of(
                new BigDecimal("1"),
                new BigDecimal("2"),
                new BigDecimal("3"),
                new BigDecimal("4.5")
        );

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("10.5");
    }

    @Test
    void shouldSumWithNegatives() {
        var operands = List.of(new BigDecimal("10"), new BigDecimal("-4"), new BigDecimal("-1"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("5");
    }

    @Test
    void shouldKeepDecimalPrecision() {
        var operands = List.of(new BigDecimal("0.10"), new BigDecimal("0.20"), new BigDecimal("0.05"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("0.35");
    }

    @Test
    void shouldReturnPlainStringWithoutScientificNotation() {
        var operands = List.of(new BigDecimal("1000000000000000000"), new BigDecimal("1"));

        String result = handler.handle(operands);

        assertThat(result).isEqualTo("1000000000000000001");
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = Mockito.spy(new AdditionOperationHandler());
        var operands = List.of(new BigDecimal("1"), new BigDecimal("2"));

        spy.handle(operands);

        verify(spy).validateOperands(operands);
    }

    // --- validateOperands() ---

    @Test
    void validateOperands_shouldThrowWhenNull() {
        assertThatThrownBy(() -> handler.validateOperands(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 2");
    }

    @Test
    void validateOperands_shouldThrowWhenEmptyList() {
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
