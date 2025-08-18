package com.fontolan.calculator.unit.application.usecases;


import com.fontolan.calculator.application.usecases.operation.impl.RandomStringOperationHandler;
import com.fontolan.calculator.infrastructure.dataprovider.RandomStringDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RandomStringOperationHandlerTest {

    private RandomStringDataProvider dataProvider;
    private RandomStringOperationHandler handler;

    @BeforeEach
    void setUp() {
        dataProvider = mock(RandomStringDataProvider.class);
        handler = new RandomStringOperationHandler(dataProvider);
    }

    @Test
    void shouldRequestRandomStringWithProvidedLength() {
        when(dataProvider.getRandomString(12)).thenReturn("abcdefghijkl");
        var result = handler.handle(List.of(new BigDecimal("12")));
        assertThat(result).isEqualTo("abcdefghijkl");
        verify(dataProvider).getRandomString(12);
        verifyNoMoreInteractions(dataProvider);
    }

    @Test
    void shouldAcceptBoundaryMinLengthOne() {
        when(dataProvider.getRandomString(1)).thenReturn("a");
        var result = handler.handle(List.of(new BigDecimal("1")));
        assertThat(result).hasSize(1).isEqualTo("a");
        verify(dataProvider).getRandomString(1);
    }

    @Test
    void shouldAcceptBoundaryMaxLengthThirtyTwo() {
        when(dataProvider.getRandomString(32)).thenReturn("x".repeat(32));
        var result = handler.handle(List.of(new BigDecimal("32")));
        assertThat(result).hasSize(32);
        verify(dataProvider).getRandomString(32);
    }

    @Test
    void shouldTruncateDecimalLengthToIntValue() {
        when(dataProvider.getRandomString(7)).thenReturn("ABCDEFG");
        var result = handler.handle(List.of(new BigDecimal("7.9")));
        assertThat(result).isEqualTo("ABCDEFG");
        verify(dataProvider).getRandomString(7);
    }

    @Test
    void shouldCallValidateOperandsInsideHandle() {
        var spy = spy(new RandomStringOperationHandler(dataProvider));
        when(dataProvider.getRandomString(5)).thenReturn("12345");
        spy.handle(List.of(new BigDecimal("5")));
        verify(spy).validateOperands(List.of(new BigDecimal("5")));
    }

    @Test
    void validateOperands_shouldThrowWhenNullList() {
        assertThatThrownBy(() -> handler.validateOperands(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
        verifyNoInteractions(dataProvider);
    }

    @Test
    void validateOperands_shouldThrowWhenEmptyList() {
        assertThatThrownBy(() -> handler.validateOperands(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
        verifyNoInteractions(dataProvider);
    }

    @Test
    void validateOperands_shouldThrowWhenMoreThanOneOperand() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("5"), new BigDecimal("6"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly 1");
        verifyNoInteractions(dataProvider);
    }

    @Test
    void validateOperands_shouldThrowWhenBelowMin() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("0"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 32");
        verifyNoInteractions(dataProvider);
    }

    @Test
    void validateOperands_shouldThrowWhenAboveMax() {
        assertThatThrownBy(() -> handler.validateOperands(List.of(new BigDecimal("33"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 32");
        verifyNoInteractions(dataProvider);
    }

    @Test
    void validateOperands_shouldPassWhenWithinRange() {
        assertThatCode(() -> handler.validateOperands(List.of(new BigDecimal("10"))))
                .doesNotThrowAnyException();
    }
}
