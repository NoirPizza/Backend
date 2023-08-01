package com.pizza.pizzashop.unit.controllers;

import com.pizza.pizzashop.controllers.PizzaController;
import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.PizzaService.PizzaService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PizzaControllerTests {
    @Mock
    private PizzaService pizzaService;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private PizzaController pizzaController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testGetAllPizzas() {
        List<PizzaDTO> pizzas = new ArrayList<>();
        pizzas.add(createPizzaDTO());
        when(pizzaService.getAllPizzas()).thenReturn(pizzas);

        ResponseEntity<SuccessDTO<List<PizzaDTO>>> responseEntity = pizzaController.getAllPizzas();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getData().size());
        assertEquals("Маргарита", responseEntity.getBody().getData().get(0).getName());
        assertEquals(200, responseEntity.getBody().getData().get(0).getWeight());
        assertEquals(800, responseEntity.getBody().getData().get(0).getPrice());
        verify(pizzaService, times(1)).getAllPizzas();
    }

    @Test
    void testGetPizzaById() throws NotFoundException {
        PizzaDTO pizzaDTO = createPizzaDTO();
        when(pizzaService.getPizzaById(1L)).thenReturn(pizzaDTO);

        ResponseEntity<SuccessDTO<PizzaDTO>> responseEntity = pizzaController.getPizzaById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Маргарита", responseEntity.getBody().getData().getName());
        assertEquals(200, responseEntity.getBody().getData().getWeight());
        assertEquals(800, responseEntity.getBody().getData().getPrice());
        verify(pizzaService, times(1)).getPizzaById(1L);
    }

    @Test
    void testGetPizzaById_NonExistingPizza() {
        when(pizzaService.getPizzaById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> pizzaController.getPizzaById(1L));
        verify(pizzaService, times(1)).getPizzaById(1L);
    }

    @Test
    void testCreatePizza() throws RequestDataValidationFailedException {
        PizzaDTO pizzaDTO = createPizzaDTO();
        when(pizzaService.createPizza(any(PizzaDTO.class))).thenReturn(pizzaDTO);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<PizzaDTO>> responseEntity = pizzaController.createPizza(pizzaDTO, bindingResult);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Маргарита", responseEntity.getBody().getData().getName());
        assertEquals(200, responseEntity.getBody().getData().getWeight());
        assertEquals(800, responseEntity.getBody().getData().getPrice());
        verify(pizzaService, times(1)).createPizza(pizzaDTO);
    }

    @Test
    void testUpdatePizza() throws RequestDataValidationFailedException, NotFoundException {
        PizzaDTO pizzaDTO = createPizzaDTO();
        when(pizzaService.updatePizza(eq(1L), any(PizzaDTO.class))).thenReturn(pizzaDTO);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<PizzaDTO>> responseEntity = pizzaController.updatePizza(1L, pizzaDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Маргарита", responseEntity.getBody().getData().getName());
        assertEquals(200, responseEntity.getBody().getData().getWeight());
        assertEquals(800, responseEntity.getBody().getData().getPrice());
        verify(pizzaService, times(1)).updatePizza(1L, pizzaDTO);
    }

    @Test
    void testUpdatePizza_NonExistingPizza() {
        PizzaDTO pizzaDTO = createPizzaDTO();
        when(pizzaService.updatePizza(eq(1L), any(PizzaDTO.class))).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThrows(NotFoundException.class, () -> pizzaController.updatePizza(1L, pizzaDTO, bindingResult));
        verify(pizzaService, times(1)).updatePizza(1L, pizzaDTO);
    }

    @Test
    void testInvalidPizzaDTO() {
        PizzaDTO pizzaDTO = createEmptyPizzaDTO();

        var violations = validator.validate(pizzaDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testDeletePizza() {
        doNothing().when(pizzaService).deletePizza(1L);
        ResponseEntity<SuccessDTO<String>> responseEntity = pizzaController.deletePizza(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully deleted pizza", responseEntity.getBody().getData());
        verify(pizzaService, times(1)).deletePizza(1L);
    }

    private PizzaDTO createPizzaDTO() {
        return new PizzaDTO(1L, "Маргарита", 200, 800, "Маргарита", "img", new ArrayList<>());
    }

    private PizzaDTO createEmptyPizzaDTO() {
        return new PizzaDTO(null, null, null, null, null, null, null);
    }
}