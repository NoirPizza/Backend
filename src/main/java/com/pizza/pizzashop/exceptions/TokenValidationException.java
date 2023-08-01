package com.pizza.pizzashop.exceptions;

/**
 * This is a custom exception class that is thrown when there is an issue with token validation.
 * This exception can be used to handle cases where a JWT token is invalid, expired, or tampered with.
 */
public class TokenValidationException extends Exception {
    public TokenValidationException(String message) {
        super(message);
    }
}
