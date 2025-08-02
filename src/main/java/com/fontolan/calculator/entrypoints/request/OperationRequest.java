package com.fontolan.calculator.entrypoints.request;

import com.fontolan.calculator.domain.enums.OperationType;
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
    private OperationType type;
    private List<BigDecimal> operands;
}
