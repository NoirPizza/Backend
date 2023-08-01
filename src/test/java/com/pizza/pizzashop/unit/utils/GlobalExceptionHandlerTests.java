package com.pizza.pizzashop.unit.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pizza.pizzashop.dtos.basic.ErrorDTO;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.utils.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

class GlobalExceptionHandlerTests {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleException() {
        Exception exception = new Exception("Test Exception");
        ResponseEntity<ErrorDTO> responseEntity = globalExceptionHandler.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getBody().getStatusCode());
        assertEquals(exception.getClass().getSimpleName(), responseEntity.getBody().getException());
        assertEquals(exception.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    void testHandlePathVariableError() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handlePathVariableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), ((ErrorDTO) responseEntity.getBody()).getStatusCode());
        assertEquals(RequestDataValidationFailedException.class.getSimpleName(), ((ErrorDTO) responseEntity.getBody()).getException());
        assertEquals(exception.getMessage(), ((ErrorDTO) responseEntity.getBody()).getMessage());
    }

    @Test
    void testHandleValidationResults() {
        FieldError fieldError1 = new FieldError("objectName", "fieldName1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", "Error message 2");

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        String expectedErrorMessage = "Validation errors: \nfieldName1: Error message 1 \nfieldName2: Error message 2";
        String actualErrorMessage = GlobalExceptionHandler.handleValidationResults(bindingResult);

        assertEquals(expectedErrorMessage, actualErrorMessage);
    }
}
