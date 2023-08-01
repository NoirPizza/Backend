package com.pizza.pizzashop.unit.controllers;

import com.pizza.pizzashop.controllers.PizzaIngredientController;
import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.PizzaIngredientService.PizzaIngredientService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PizzaIngredientControllerTests {
    @Mock
    private PizzaIngredientService ingredientService;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private PizzaIngredientController ingredientController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testGetAllIngredients() {
        List<PizzaIngredientDTO> ingredients = Arrays.asList(
                new PizzaIngredientDTO(1L, "Томаты", 20),
                new PizzaIngredientDTO(2L, "Сыр", 30)
        );
        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        ResponseEntity<SuccessDTO<List<PizzaIngredientDTO>>> responseEntity = ingredientController.getAllIngredients();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Pizza Ingredient Get All", responseEntity.getBody().getSubject());
        assertEquals(ingredients, responseEntity.getBody().getData());
        verify(ingredientService, times(1)).getAllIngredients();
    }

    @Test
    void testGetIngredientById() throws NotFoundException {
        PizzaIngredientDTO ingredient = new PizzaIngredientDTO(1L, "Томаты", 20);
        when(ingredientService.getIngredientById(1L)).thenReturn(ingredient);

        ResponseEntity<SuccessDTO<PizzaIngredientDTO>> responseEntity = ingredientController.getIngredientById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Pizza Ingredient Get By Id", responseEntity.getBody().getSubject());
        assertEquals(ingredient, responseEntity.getBody().getData());
        verify(ingredientService, times(1)).getIngredientById(1L);
    }

    @Test
    void testGetIngredientById_NonExistingIngredient() {
        when(ingredientService.getIngredientById(100L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> ingredientController.getIngredientById(100L));
        verify(ingredientService, times(1)).getIngredientById(100L);
    }

    @Test
    void testCreateIngredient() throws RequestDataValidationFailedException {
        PizzaIngredientDTO newIngredient = new PizzaIngredientDTO(null, "Сыр", 20);
        PizzaIngredientDTO createdIngredient = new PizzaIngredientDTO(3L, "Сыр", 20);
        when(ingredientService.createIngredient(newIngredient)).thenReturn(createdIngredient);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<PizzaIngredientDTO>> responseEntity = ingredientController.createIngredient(newIngredient, bindingResult);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Pizza Ingredient Create", responseEntity.getBody().getSubject());
        assertEquals(createdIngredient, responseEntity.getBody().getData());
        verify(ingredientService, times(1)).createIngredient(newIngredient);
    }

    @Test
    void testUpdateIngredient() throws RequestDataValidationFailedException, NotFoundException {
        PizzaIngredientDTO updatedIngredient = new PizzaIngredientDTO(2L, "Cыр", 20);
        PizzaIngredientDTO returnedIngredient = new PizzaIngredientDTO(2L, "Сыр", 20);
        when(ingredientService.updateIngredient(2L, updatedIngredient)).thenReturn(returnedIngredient);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<PizzaIngredientDTO>> responseEntity = ingredientController.updatePizza(2L, updatedIngredient, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Pizza Ingredient Update", responseEntity.getBody().getSubject());
        assertEquals(returnedIngredient, responseEntity.getBody().getData());
        verify(ingredientService, times(1)).updateIngredient(2L, updatedIngredient);
    }

    @Test
    void testUpdateIngredient_NonExistingIngredient() {
        PizzaIngredientDTO updatedIngredient = new PizzaIngredientDTO(100L, "Invalid Ingredient", 20);
        when(ingredientService.updateIngredient(100L, updatedIngredient)).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ingredientController.updatePizza(100L, updatedIngredient, bindingResult));
        verify(ingredientService, times(1)).updateIngredient(100L, updatedIngredient);
    }

    @Test
    void testInvalidPizzaIngredientDTO() {
        PizzaIngredientDTO pizzaIngredientDTO = new PizzaIngredientDTO(null, null, null);

        var violations = validator.validate(pizzaIngredientDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testDeleteIngredient() {
        ResponseEntity<SuccessDTO<String>> responseEntity = ingredientController.deleteIngredient(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Pizza Ingredient Delete", responseEntity.getBody().getSubject());
        assertEquals("Successfully deleted pizza ingredient", responseEntity.getBody().getData());
        verify(ingredientService, times(1)).deleteIngredient(1L);
    }
}
