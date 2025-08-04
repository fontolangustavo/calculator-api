package com.fontolan.calculator.entrypoints.mapper;

import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getStatus()
        );
    }
}
