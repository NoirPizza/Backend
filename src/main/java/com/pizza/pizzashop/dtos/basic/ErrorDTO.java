package com.pizza.pizzashop.dtos.basic;

import java.io.Serializable;

/**
 * This class represents a data transfer object (DTO) that encapsulates error information in a standardized format.
 * It is used to provide a consistent structure for reporting errors and exceptions in the application.
 */
public class ErrorDTO implements Serializable {
    private final Integer statusCode;

    private final String exception;
    private final String message;

    public ErrorDTO(Integer statusCode, String exception, String message) {
        this.statusCode = statusCode;
        this.exception = exception;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }
}
