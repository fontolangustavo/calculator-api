package com.fontolan.calculator.infrastructure.mapper;


import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getStatus(),
                entity.getBalance()
        );
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setStatus(user.getStatus());
        entity.setBalance(user.getBalance());
        return entity;
    }
}