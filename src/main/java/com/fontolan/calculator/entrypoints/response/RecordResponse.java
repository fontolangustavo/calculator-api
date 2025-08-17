package com.fontolan.calculator.entrypoints.response;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class RecordResponse {
    private UUID id;
    private OperationType operationType;
    private String amount;
    private String operationResponse;
    private BigDecimal userBalance;
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
    public String getOperationResponse() { return operationResponse; }
    public void setOperationResponse(String operationResponse) { this.operationResponse = operationResponse; }
    public BigDecimal getUserBalance() { return userBalance; }
    public void setUserBalance(BigDecimal userBalance) { this.userBalance = userBalance; }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}