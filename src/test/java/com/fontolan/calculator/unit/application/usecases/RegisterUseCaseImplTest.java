package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.auth.impl.RegisterUseCaseImpl;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RegisterUseCaseImplTest {

    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUseCaseImpl registerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUserWhenUsernameDoesNotExist() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("new_user");
        request.setPassword("plainSecret");

        when(userDataProvider.findByUsername("new_user")).thenReturn(null);
        when(passwordEncoder.encode("plainSecret")).thenReturn("ENCODED");

        User saved = new User(
                java.util.UUID.randomUUID(),
                "new_user",
                "ENCODED",
                UserStatus.ACTIVE,
                BigDecimal.TEN
        );
        when(userDataProvider.save(any(User.class))).thenReturn(saved);

        User result = registerUseCase.execute(request);

        assertNotNull(result);
        assertEquals("new_user", result.getUsername());
        assertEquals("ENCODED", result.getPassword());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertEquals(BigDecimal.TEN, result.getBalance());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDataProvider).save(userCaptor.capture());
        User toSave = userCaptor.getValue();

        assertNull(toSave.getId(), "id deve ser nulo antes de persistir");
        assertEquals("new_user", toSave.getUsername());
        assertEquals("ENCODED", toSave.getPassword(), "senha deve estar codificada");
        assertEquals(UserStatus.ACTIVE, toSave.getStatus());
        assertEquals(BigDecimal.TEN, toSave.getBalance());

        verify(userDataProvider, times(1)).findByUsername("new_user");
        verify(passwordEncoder, times(1)).encode("plainSecret");
        verify(userDataProvider, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userDataProvider, passwordEncoder);
    }

    @Test
    void shouldThrowBusinessExceptionWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setPassword("whatever");

        when(userDataProvider.findByUsername("existing"))
                .thenReturn(new User(UUID.randomUUID(), "existing", "MOCK_PASSWORD", UserStatus.ACTIVE, BigDecimal.TEN)); // já existe

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> registerUseCase.execute(request)
        );
        assertTrue(ex.getMessage().contains("Username already exists"));

        verify(userDataProvider, times(1)).findByUsername("existing");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userDataProvider, never()).save(any(User.class));
        verifyNoMoreInteractions(userDataProvider, passwordEncoder);
    }

    @Test
    void shouldPropagateExceptionIfPasswordEncoderFails() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("new_user");
        request.setPassword("plain");

        when(userDataProvider.findByUsername("new_user")).thenReturn(null);
        when(passwordEncoder.encode("plain")).thenThrow(new RuntimeException("encoder down"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> registerUseCase.execute(request)
        );
        assertEquals("encoder down", ex.getMessage());

        verify(userDataProvider).findByUsername("new_user");
        verify(passwordEncoder).encode("plain");
        verify(userDataProvider, never()).save(any(User.class));
        verifyNoMoreInteractions(userDataProvider, passwordEncoder);
    }
}