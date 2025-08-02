package com.fontolan.calculator.unit.entrypoints.controllers;

import com.fontolan.calculator.application.usecases.auth.LoginUseCase;
import com.fontolan.calculator.entrypoints.controllers.impl.AuthControllerImpl;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthControllerImplTest {

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private AuthControllerImpl authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginReturnsJwtResponse() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");

        JwtResponse expectedResponse = new JwtResponse("token123");

        when(loginUseCase.execute(request)).thenReturn(expectedResponse);

        ResponseEntity<JwtResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(loginUseCase, times(1)).execute(request);
    }
}
