package com.fontolan.calculator.entrypoints.request;

import com.fontolan.calculator.domain.enums.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(name = "OperationRequest")
public class OperationRequest {
    @Schema(
            description = "Type of operation to perform",
            implementation = OperationType.class,
            example = "DIVISION"
    )
    @NotNull(message = "Operation type cannot be null")
    private OperationType type;

    @Schema(description = "Operands required by the operation", example = "[100, 5]")
    private List<@NotNull(message = "Operand cannot be null") BigDecimal> operands;
}
