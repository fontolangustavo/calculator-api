package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.controllers.docs.AuthConstants;
import com.fontolan.calculator.entrypoints.controllers.docs.StandardErrors;
import com.fontolan.calculator.entrypoints.controllers.docs.StandardErrorsSecured;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import com.fontolan.calculator.entrypoints.request.RegisterRequest;
import com.fontolan.calculator.entrypoints.response.JwtResponse;
import com.fontolan.calculator.entrypoints.response.RegisterResponse;
import com.fontolan.calculator.entrypoints.response.UserResponse;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication and user registration")
public interface AuthController {

    @Operation(
            summary = "Login",
            description = "Authenticate a user and return the JWT (access token).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(name = "login", value = AuthConstants.LOGIN_REQ)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class),
                            examples = @ExampleObject(name = "login-ok", value = AuthConstants.LOGIN_RES_OK)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable Entity",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "login-invalid-credentials", value = AuthConstants.LOGIN_RES_INVALID_CREDENTIALS)
                    )
            )
    })
    @StandardErrors
    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request);

    @Operation(
            summary = "Register a new user",
            description = "Create a new user account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(name = "register", value = AuthConstants.REGISTER_REQ)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class),
                            examples = @ExampleObject(name = "register-ok", value = AuthConstants.REGISTER_RES_OK)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Unprocessable Entity",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "register-already-exists", value = AuthConstants.REGISTER_RES_UNPROCESSABLE_ENTITY)
                    )
            ),
    })
    @StandardErrors
    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request);

    @Operation(
            summary = "Get current user profile",
            description = "Returns the authenticated user's profile."
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(name = "me-ok", value = AuthConstants.ME_RES_OK)
            )
    )
    @StandardErrorsSecured
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal String username
    );
}
