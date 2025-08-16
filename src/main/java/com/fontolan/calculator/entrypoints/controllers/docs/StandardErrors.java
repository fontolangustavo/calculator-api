package com.fontolan.calculator.entrypoints.controllers.docs;

import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Bad request / validation / arithmetic error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = @ExampleObject(name = "validation-error", value = ExampleConstants.VALIDATION_ERR)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                headers = @Header(
                        name = "WWW-Authenticate",
                        description = "Auth challenge",
                        schema = @Schema(type = "string", example = "Bearer realm=\"api\", error=\"invalid_token\"")
                ),
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = @ExampleObject(name = "unauthorized", value = ExampleConstants.UNAUTHORIZED_ERR)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = @ExampleObject(name = "forbidden", value = ExampleConstants.FORBIDDEN_ERR)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = @ExampleObject(name = "unexpected", value = ExampleConstants.UNEXPECTED_ERR)
                )
        )
})
public @interface StandardErrors {}
