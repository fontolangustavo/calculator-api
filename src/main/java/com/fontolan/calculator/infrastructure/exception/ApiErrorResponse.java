package com.fontolan.calculator.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status","message","errors"})
@Schema(name = "ApiErrorResponse", description = "Standard API error payload")
public record ApiErrorResponse(
        @Schema(example = "401") int status,
        @Schema(example = "Missing or invalid token") String message,
        @Schema(description = "Additional error details (e.g., path, field errors)") Map<String, Object> errors
) {
    public static ApiErrorResponse unauthorized(String path) {
        return new ApiErrorResponse(401, "Missing or invalid token", path != null ? Map.of("path", path) : null);
    }

    public static ApiErrorResponse forbidden(String path) {
        return new ApiErrorResponse(403, "Access denied", path != null ? Map.of("path", path) : null);
    }

    public static ApiErrorResponse validation(String message, Map<String, ?> fieldErrors) {
        Map<String, Object> details = new LinkedHashMap<>();
        if (fieldErrors != null) {
            details.putAll(fieldErrors);
        }
        return new ApiErrorResponse(400, message != null ? message : "Validation failed", details.isEmpty() ? null : details);
    }

    public static ApiErrorResponse validation(String message, List<Map.Entry<String, String>> fieldErrors) {
        Map<String, Object> details = new LinkedHashMap<>();
        if (fieldErrors != null) {
            for (Map.Entry<String, String> e : fieldErrors) {
                details.put(e.getKey(), e.getValue());
            }
        }
        return new ApiErrorResponse(400, message != null ? message : "Validation failed", details.isEmpty() ? null : details);
    }

    public static ApiErrorResponse simple(int status, String message, String path) {
        return new ApiErrorResponse(status, message, path != null ? Map.of("path", path) : null);
    }
}
