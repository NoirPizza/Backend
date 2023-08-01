package com.pizza.pizzashop.exceptions;

/**
 * This is a custom exception class that is thrown when validation fails.
 * This exception can be used to handle cases where input data or entities fail to pass validation rules or constraints.
 */
public class RequestDataValidationFailedException extends Exception {
    public RequestDataValidationFailedException(String message) {
        super(message);
    }
}
