package com.fontolan.calculator.unit.application.usecases;


import com.fontolan.calculator.application.usecases.auth.impl.GetUserUseCaseImpl;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetUserUseCaseImplTest {

    @Mock
    private UserDataProvider userDataProvider;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserWhenFoundByUsername() {
        String username = "john_doe";
        User expectedUser = new User(UUID.randomUUID(), username, "MOCK_PASSWORD", UserStatus.ACTIVE, BigDecimal.TEN);

        when(userDataProvider.findByUsername(username)).thenReturn(expectedUser);

        User result = getUserUseCase.execute(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userDataProvider, times(1)).findByUsername(username);
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        String username = "unknown_user";
        when(userDataProvider.findByUsername(username)).thenReturn(null);

        User result = getUserUseCase.execute(username);

        assertNull(result);
        verify(userDataProvider, times(1)).findByUsername(username);
    }
}
