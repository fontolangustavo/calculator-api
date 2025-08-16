package com.fontolan.calculator.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supported operation types")
public enum OperationType {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    SQUARE_ROOT,
    RANDOM_STRING;
}
