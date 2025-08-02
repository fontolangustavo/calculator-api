package com.fontolan.calculator.infrastructure.dataprovider;


import com.fontolan.calculator.domain.model.User;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserDataProvider {
    User findByUsername(String username);
    void updateBalance(UUID userId, BigDecimal newBalance);
}