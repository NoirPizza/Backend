package com.pizza.pizzashop.exceptions;

/**
 * This is a custom exception class that is thrown when authentication fails.
 * This exception can be used to handle cases where a user's authentication credentials are invalid or not recognized.
 */
public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
