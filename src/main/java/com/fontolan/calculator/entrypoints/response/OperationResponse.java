package com.fontolan.calculator.entrypoints.response;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;

public class OperationResponse {
    private OperationType type;
    private String input;
    private String result;
    private BigDecimal userBalance;

    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }
    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public BigDecimal getUserBalance() { return userBalance; }
    public void setUserBalance(BigDecimal userBalance) { this.userBalance = userBalance; }
}
