package com.fontolan.calculator.unit.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.entity.UserEntity;
import com.fontolan.calculator.infrastructure.dataprovider.impl.UserDataProviderImpl;
import com.fontolan.calculator.infrastructure.dataprovider.repository.UserRepository;
import com.fontolan.calculator.infrastructure.mapper.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDataProviderImplTest {

    private UserRepository userRepository;
    private UserEntityMapper userEntityMapper;
    private UserDataProviderImpl userDataProvider;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userEntityMapper = mock(UserEntityMapper.class);
        userDataProvider = new UserDataProviderImpl(userRepository, userEntityMapper);
    }

    @Test
    void shouldReturnUserWhenUserExists() {
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        User domainUser = mockUser();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.toDomain(userEntity)).thenReturn(domainUser);

        User result = userDataProvider.findByUsername(username);

        assertNotNull(result);
        assertEquals(domainUser, result);
        verify(userRepository).findByUsername(username);
        verify(userEntityMapper).toDomain(userEntity);
    }

    @Test
    void shouldReturnNullWhenUserDoesNotExist() {
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User result = userDataProvider.findByUsername(username);

        assertNull(result);
        verify(userRepository).findByUsername(username);
        verify(userEntityMapper, never()).toDomain(any());
    }

    private User mockUser() {
        return new User(
                UUID.randomUUID(),
                "user1",
                "password123",
                UserStatus.ACTIVE,
                BigDecimal.valueOf(100L)
        );
    }
}
