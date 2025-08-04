package com.fontolan.calculator.domain.exception;

public class BaseApiException extends RuntimeException {
    private final int statusCode;
    private final String errorCode;

    public BaseApiException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}