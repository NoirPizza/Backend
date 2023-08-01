package com.pizza.pizzashop.utils;

import com.pizza.pizzashop.dtos.basic.ErrorDTO;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is a controller advice class that handles exceptions globally for the application.
 * It provides a centralized way to handle exceptions and return a consistent error response to clients.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles all exceptions that can possibly occur while the application works.
     *
     * @param e The exception to be handled.
     * @return A ResponseEntity containing the ErrorDTO with the error details and HTTP status code 400 (Bad Request).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getClass().getSimpleName(), e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Separately handles all exceptions of type ConstraintViolationException that occur during validation of request data
     * (e.g. RequestBody, RequestParams, PathVariable)
     *
     * @param e The ConstraintViolationException to be handled as RequestDataValidationFailedException.
     * @return A ResponseEntity containing the ErrorDTO with the error details and HTTP status code 400 (Bad Request).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handlePathVariableException(ConstraintViolationException e) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST.value(),
                RequestDataValidationFailedException.class.getSimpleName(),
                e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);
    }

    /**
     * Creates a formatted error message from the given BindingResult containing validation errors.
     * The resulting message will have the format: "Validation errors:\n[fieldName]: [errorMessage]\n[fieldName]: [errorMessage]..."
     *
     * @param validationResult The BindingResult object containing validation errors.
     * @return A String representing the formatted error message with field names and error messages.
     */
    public static String handleValidationResults(BindingResult validationResult) {
        StringBuilder message = new StringBuilder("Validation errors:");
        for (FieldError error: validationResult.getFieldErrors()) {
            message.append(" \n").append(error.getField()).append(": ").append(error.getDefaultMessage());
        }
        return message.toString();
    }
}
