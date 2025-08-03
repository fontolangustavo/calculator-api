package com.fontolan.calculator.entrypoints.request;

import com.fontolan.calculator.domain.enums.OperationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationRequest {
    @NotNull(message = "Operation type cannot be null")
    private OperationType type;

    @NotEmpty(message = "Operands list cannot be empty")
    @Size(min = 1, message = "At least one operand is required")
    private List<@NotNull(message = "Operand cannot be null") BigDecimal> operands;
}
