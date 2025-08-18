package com.fontolan.calculator.unit.infrastructure.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorResponseTest {

    @Test
    void unauthorized_withPath() {
        ApiErrorResponse res = ApiErrorResponse.unauthorized("/api/x");
        assertThat(res.status()).isEqualTo(401);
        assertThat(res.message()).isEqualTo("Missing or invalid token");
        assertThat(res.errors()).containsEntry("path", "/api/x");
    }

    @Test
    void unauthorized_withoutPath() {
        ApiErrorResponse res = ApiErrorResponse.unauthorized(null);
        assertThat(res.status()).isEqualTo(401);
        assertThat(res.message()).isEqualTo("Missing or invalid token");
        assertThat(res.errors()).isNull();
    }

    @Test
    void forbidden_withPath() {
        ApiErrorResponse res = ApiErrorResponse.forbidden("/secure");
        assertThat(res.status()).isEqualTo(403);
        assertThat(res.message()).isEqualTo("Access denied");
        assertThat(res.errors()).containsEntry("path", "/secure");
    }

    @Test
    void simple_withPath() {
        ApiErrorResponse res = ApiErrorResponse.simple(422, "Invalid op", "/ops");
        assertThat(res.status()).isEqualTo(422);
        assertThat(res.message()).isEqualTo("Invalid op");
        assertThat(res.errors()).containsEntry("path", "/ops");
    }

    @Test
    void simple_withoutPath() {
        ApiErrorResponse res = ApiErrorResponse.simple(500, "Boom", null);
        assertThat(res.status()).isEqualTo(500);
        assertThat(res.message()).isEqualTo("Boom");
        assertThat(res.errors()).isNull();
    }

    @Test
    void validation_withMap() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("username", "must not be blank");
        fields.put("age", "must be >= 18");
        ApiErrorResponse res = ApiErrorResponse.validation("Invalid payload", fields);
        assertThat(res.status()).isEqualTo(400);
        assertThat(res.message()).isEqualTo("Invalid payload");
        assertThat(res.errors()).containsEntry("username", "must not be blank")
                .containsEntry("age", "must be >= 18");
    }

    @Test
    void validation_withEmptyMap_shouldSetErrorsNull() {
        ApiErrorResponse res = ApiErrorResponse.validation("Invalid", Map.of());
        assertThat(res.status()).isEqualTo(400);
        assertThat(res.message()).isEqualTo("Invalid");
        assertThat(res.errors()).isNull();
    }

    @Test
    void validation_withNullMap_shouldSetErrorsNull() {
        ApiErrorResponse res = ApiErrorResponse.validation("Invalid", (Map<String, ?>) null);
        assertThat(res.status()).isEqualTo(400);
        assertThat(res.message()).isEqualTo("Invalid");
        assertThat(res.errors()).isNull();
    }

    @Test
    void validation_withEntriesList() {
        List<Map.Entry<String, String>> entries = List.of(
                Map.entry("field1", "error1"),
                Map.entry("field2", "error2")
        );
        ApiErrorResponse res = ApiErrorResponse.validation("Validation failed", entries);
        assertThat(res.status()).isEqualTo(400);
        assertThat(res.message()).isEqualTo("Validation failed");
        assertThat(res.errors()).containsEntry("field1", "error1")
                .containsEntry("field2", "error2");
    }

    @Test
    void validation_withNullEntries_shouldSetErrorsNull() {
        ApiErrorResponse res = ApiErrorResponse.validation(null, (List<Map.Entry<String, String>>) null);
        assertThat(res.status()).isEqualTo(400);
        assertThat(res.message()).isEqualTo("Validation failed");
        assertThat(res.errors()).isNull();
    }

    @Test
    void json_shouldOmitErrorsWhenNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ApiErrorResponse res = ApiErrorResponse.simple(401, "Missing or invalid token", null);
        String json = mapper.writeValueAsString(res);
        assertThat(json).contains("\"status\":401");
        assertThat(json).contains("\"message\":\"Missing or invalid token\"");
        assertThat(json).doesNotContain("errors");
    }

    @Test
    void json_shouldIncludeErrorsWhenPresent() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ApiErrorResponse res = ApiErrorResponse.unauthorized("/api/v1/x");
        String json = mapper.writeValueAsString(res);
        assertThat(json).contains("\"errors\":{\"path\":\"/api/v1/x\"}");
    }
}
