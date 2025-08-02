package com.fontolan.calculator.infrastructure.dataprovider.impl;


import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.repository.UserRepository;
import com.fontolan.calculator.infrastructure.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;

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
                .orElse(null);
    }
}