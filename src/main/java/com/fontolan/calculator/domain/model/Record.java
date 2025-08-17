package com.fontolan.calculator.domain.model;

import com.fontolan.calculator.domain.enums.OperationType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Record {
    private UUID id;
    private UUID userId;
    private OperationType operationType;
    private String amount;
    private BigDecimal userBalance;
    private String operationResponse;
    private Instant createdAt;
    private Instant deletedAt;


    public Record(UUID id, UUID userId, OperationType operationType, String amount, BigDecimal userBalance, String operationResponse, Instant createdAt, Instant deletedAt) {
        this.id = id;
        this.userId = userId;
        this.operationType = operationType;
        this.amount = amount;
        this.userBalance = userBalance;
        this.operationResponse = operationResponse;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    public String getOperationResponse() {
        return operationResponse;
    }

    public void setOperationResponse(String operationResponse) {
        this.operationResponse = operationResponse;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() { return deletedAt != null; }
    public void markDeleted() { this.deletedAt = Instant.now(); }
}
