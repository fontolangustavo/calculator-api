package com.fontolan.calculator.infrastructure.dataprovider;


import com.fontolan.calculator.domain.model.User;

public interface UserDataProvider {
    User findByUsername(String username);
}