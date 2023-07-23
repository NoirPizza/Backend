package com.pizza.pizzashop.dtos;

import java.io.Serializable;

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
