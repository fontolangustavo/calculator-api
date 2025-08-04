package com.fontolan.calculator.domain.model;

import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private UserStatus status;
    private BigDecimal balance;

    public User(UUID id, String username, String password, UserStatus status, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserStatus getStatus() { return status; }
    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public boolean isActive() { return status == UserStatus.ACTIVE; }

    public void deductBalance(BigDecimal cost) {
        if (balance.compareTo(cost) < 0) {
            throw new BusinessException("Insufficient balance for operation");
        }

        balance = balance.subtract(cost);
    }
}
