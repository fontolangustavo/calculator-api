package com.fontolan.calculator.entrypoints.response;

import com.fontolan.calculator.domain.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationTypeResponse {
    private Long id;
    private OperationType type;
    private BigDecimal cost;
}
