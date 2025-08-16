package com.fontolan.calculator.infrastructure.external.randomorg.exception;

public class RandomOrgException extends RuntimeException {
    private final Integer randomOrgCode;
    private final int httpStatus;

    public RandomOrgException(Integer randomOrgCode, String message, int httpStatus) {
        super(message);
        this.randomOrgCode = randomOrgCode;
        this.httpStatus = httpStatus;
    }

    public Integer getRandomOrgCode() { return randomOrgCode; }
    public int getHttpStatus() { return httpStatus; }
}

