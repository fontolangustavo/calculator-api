package com.fontolan.calculator.unit.infrastructure.exception;

import com.fontolan.calculator.domain.exception.BaseApiException;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.infrastructure.exception.ApiErrorResponse;
import com.fontolan.calculator.infrastructure.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleBaseApiException_shouldReturnConfiguredStatusAndBody() {
        BaseApiException ex = mock(BaseApiException.class);
        when(ex.getStatusCode()).thenReturn(401);
        when(ex.getMessage()).thenReturn("Missing or invalid token");

        ResponseEntity<ApiErrorResponse> resp = handler.handleBaseApiException(ex, request);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().message()).isEqualTo("Missing or invalid token");
        assertThat(resp.getBody().errors()).isInstanceOf(Map.class);
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithFieldErrors() throws NoSuchMethodException {
        Dummy target = new Dummy();
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(target, "dummy");
        br.addError(new FieldError("dummy", "username", "must not be blank"));
        br.addError(new FieldError("dummy", "age", "must be >= 18"));

        Method m = DummyMethods.class.getDeclaredMethod("method", Dummy.class);
        MethodParameter param = new MethodParameter(m, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(param, br);

        ResponseEntity<ApiErrorResponse> resp = handler.handleValidationExceptions(ex, request);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().message()).isEqualTo("Validation failed");

        @SuppressWarnings("unchecked")
        Map<String, Object> errors = (Map<String, Object>) resp.getBody().errors();
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("username", "must not be blank");
        expected.put("age", "must be >= 18");

        assertThat(errors).containsExactlyEntriesOf(expected);
    }

    @Test
    void handleBusinessException_shouldReturnUnprocessableEntity() {
        BusinessException ex = mock(BusinessException.class);
        when(ex.getMessage()).thenReturn("Insufficient balance");

        ResponseEntity<ApiErrorResponse> resp = handler.handleBusinessException(ex, request);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().message()).isEqualTo("Insufficient balance");
    }

    @Test
    void handleRuntimeException_shouldReturnInternalServerErrorWithMessage() {
        RuntimeException ex = new RuntimeException("Boom");

        ResponseEntity<ApiErrorResponse> resp = handler.handleRuntimeException(ex, request);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().message()).isEqualTo("Boom");
    }

    @Test
    void handleRuntimeException_shouldReturnInternalServerErrorWithDefaultMessageWhenNull() {
        RuntimeException ex = new RuntimeException();

        ResponseEntity<ApiErrorResponse> resp = handler.handleRuntimeException(ex, request);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().message()).isEqualTo("Unexpected error");
    }

    static class Dummy {}

    static class DummyMethods {
        public void method(Dummy dummy) {}
    }
}
