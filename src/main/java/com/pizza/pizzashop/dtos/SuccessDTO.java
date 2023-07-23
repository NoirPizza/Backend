package com.pizza.pizzashop.dtos;

import java.io.Serializable;

public class SuccessDTO implements Serializable {
    private final Integer statusCode;

    private final String trigger;
    private final String message;

    public SuccessDTO(Integer statusCode, String trigger, String message) {
        this.statusCode = statusCode;
        this.trigger = trigger;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getMessage() {
        return message;
    }
}
