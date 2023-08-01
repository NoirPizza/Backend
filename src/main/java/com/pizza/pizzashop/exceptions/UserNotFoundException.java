package com.pizza.pizzashop.exceptions;

/**
 * This is a custom exception class that is thrown when a user is not found.
 * This exception can be used to handle cases where an operation or query is performed on a user that does not exist in the system.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
