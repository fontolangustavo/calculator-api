package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.auth.impl.LoginUseCaseImpl;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.InvalidCredentialsException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginUseCaseImplTest {

    private UserDataProvider userDataProvider;
    private LoginUseCaseImpl loginUseCase;

    @BeforeEach
    void setUp() {
        userDataProvider = mock(UserDataProvider.class);
        loginUseCase = new LoginUseCaseImpl(userDataProvider);
    }

    @Test
    void shouldReturnJwtResponseWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("password123");

        User user = mockUser();

        when(userDataProvider.findByUsername("user1")).thenReturn(user);

        JwtResponse response = loginUseCase.execute(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(userDataProvider, times(1)).findByUsername("user1");
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("invalidUser");
        request.setPassword("password");

        when(userDataProvider.findByUsername("invalidUser")).thenReturn(null);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            loginUseCase.execute(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("wrongPassword");

        User user = mockUser();

        when(userDataProvider.findByUsername("user1")).thenReturn(user);

        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            loginUseCase.execute(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
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
