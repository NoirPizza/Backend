package com.pizza.pizzashop.exceptions;

/**
 * This is a custom exception class that is thrown when a resource is not found.
 * This exception can be used to handle cases where an expected resource or entity is not present or cannot be located.
 */
public class NotFoundException extends Exception{

    public NotFoundException(String message) {
        super(message);
    }
}
