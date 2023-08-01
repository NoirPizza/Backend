package com.pizza.pizzashop.dtos.basic;

import java.io.Serializable;

/**
 * This class represents a data transfer object (DTO) that encapsulates successful response information in a standardized format.
 * It is used to provide a consistent structure for sending successful responses with status code, subject, and data in the application.
 * SuccessDTO is a generic class, allowing different types of data to be included in the response.
 *
 * @param <T> The type of data included in the response.
 */
public class SuccessDTO<T> implements Serializable {
    private final Integer statusCode;
    private final String subject;
    private final T data;

    public SuccessDTO(Integer statusCode, String subject, T data) {
        this.statusCode = statusCode;
        this.subject = subject;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getSubject() {
        return subject;
    }

    public T getData() {
        return data;
    }
}
