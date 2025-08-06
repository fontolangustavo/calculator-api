package com.fontolan.calculator.application.usecases.auth.impl;

import com.fontolan.calculator.application.usecases.auth.GetUserUseCase;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.springframework.stereotype.Service;

@Service
public class GetUserUseCaseImpl implements GetUserUseCase {
    private final UserDataProvider userDataProvider;

    public GetUserUseCaseImpl(UserDataProvider userDataProvider) {
        this.userDataProvider = userDataProvider;
    }

    @Override
    public User execute(String username) {
        return userDataProvider.findByUsername(username);
    }
}
