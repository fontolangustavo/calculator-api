package com.fontolan.calculator.infrastructure.exception;

import com.fontolan.calculator.domain.exception.BaseApiException;
import com.fontolan.calculator.domain.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseApiException(BaseApiException ex, HttpServletRequest req) {
        var body = ApiErrorResponse.simple(ex.getStatusCode(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> fields.put(err.getField(), err.getDefaultMessage()));
        var body = ApiErrorResponse.validation("Validation failed", fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest req) {
        var body = ApiErrorResponse.simple(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(), req.getRequestURI());
        return ResponseEntity.unprocessableEntity().body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        var body = ApiErrorResponse.simple(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error",
                req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}