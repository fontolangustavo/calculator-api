package com.fontolan.calculator.domain.model;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Record {
    private UUID id;
    private UUID userId;
    private OperationType operationType;
    private String amount;
    private BigDecimal userBalance;
    private String operationResponse;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public Record(UUID id, UUID userId, OperationType operationType, String amount, BigDecimal userBalance, String operationResponse, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.operationType = operationType;
        this.amount = amount;
        this.userBalance = userBalance;
        this.operationResponse = operationResponse;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public OperationType getOperationType() { return operationType; }
    public String getAmount() { return amount; }
    public BigDecimal getUserBalance() { return userBalance; }
    public String getOperationResponse() { return operationResponse; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public boolean isDeleted() { return deletedAt != null; }
    public void markDeleted() { this.deletedAt = LocalDateTime.now(); }
}
