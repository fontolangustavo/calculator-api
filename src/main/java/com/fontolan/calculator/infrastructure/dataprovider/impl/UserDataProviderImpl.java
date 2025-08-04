package com.fontolan.calculator.infrastructure.dataprovider.impl;


import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.repository.UserRepository;
import com.fontolan.calculator.infrastructure.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class UserDataProviderImpl implements UserDataProvider {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserDataProviderImpl(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userEntityMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    @Override
    public void updateBalance(UUID userId, BigDecimal newBalance) {
        var entity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by ID: " + userId));

        entity.setBalance(newBalance);

        userRepository.save(entity);
    }
}