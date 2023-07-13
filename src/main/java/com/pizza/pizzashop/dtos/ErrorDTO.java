package com.pizza.pizzashop.dtos;

public class ErrorDTO {
    private final Integer statusCode;
    private final String message;


    public ErrorDTO(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
