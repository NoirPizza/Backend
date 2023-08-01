package com.pizza.pizzashop.unit.services;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.entities.Pizza;
import com.pizza.pizzashop.entities.PizzaIngredient;
import com.pizza.pizzashop.mappers.PizzaMapper;
import com.pizza.pizzashop.repositories.PizzaIngredientRepository;
import com.pizza.pizzashop.repositories.PizzaRepository;
import com.pizza.pizzashop.services.PizzaService.PizzaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PizzaServiceTests {
    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private PizzaIngredientRepository ingredientRepository;
    @Mock
    private PizzaMapper pizzaMapper;
    @InjectMocks
    private PizzaServiceImpl pizzaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPizzas() {
        List<Pizza> pizzas = Arrays.asList(
                new Pizza(1L, "Маргарита", 300, 10, "cool", "margherita.jpg", new ArrayList<>()),
                new Pizza(2L, "Пепперони", 400, 12, "cool2", "pepperoni.jpg", new ArrayList<>())
        );
        when(pizzaRepository.findAll()).thenReturn(pizzas);

        List<PizzaDTO> pizzaDTOs = Arrays.asList(
                new PizzaDTO(1L, "Маргарита", 300, 10, "cool", "margherita.jpg", null),
                new PizzaDTO(2L, "Пепперони", 400, 12, "cool2", "pepperoni.jpg", null)
        );
        when(pizzaMapper.toDTO(any(Pizza.class))).thenReturn(pizzaDTOs.get(0), pizzaDTOs.get(1));

        List<PizzaDTO> result = pizzaService.getAllPizzas();

        assertEquals(2, result.size());
        assertEquals(pizzaDTOs, result);
        verify(pizzaRepository, times(1)).findAll();
    }

    @Test
    void testGetPizzaById() {
        Pizza pizza = new Pizza(1L, "Маргарита", 300, 10, "cool", "margherita.jpg", new ArrayList<>());
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));

        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Маргарита", 300, 10, "cool", "margherita.jpg", new ArrayList<>());
        when(pizzaMapper.toDTO(pizza)).thenReturn(pizzaDTO);

        PizzaDTO result = pizzaService.getPizzaById(1L);

        assertNotNull(result);
        assertEquals(pizzaDTO, result);
        verify(pizzaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPizzaById_PizzaNotFound() {
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        PizzaDTO result = pizzaService.getPizzaById(1L);

        assertNull(result);
        verify(pizzaRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePizza() {
        PizzaIngredientDTO ingredientDTO1 = new PizzaIngredientDTO(1L, "Томаты", 20);
        PizzaIngredientDTO ingredientDTO2 = new PizzaIngredientDTO(2L, "Сыр", 30);
        List<PizzaIngredientDTO> ingredients = Arrays.asList(ingredientDTO1, ingredientDTO2);

        PizzaDTO pizzaDTO = new PizzaDTO(1L, "Маргарита", 300, 10, "cool", "img", ingredients);

        PizzaIngredient ingredient1 = new PizzaIngredient(1L, "Томаты", 20);
        PizzaIngredient ingredient2 = new PizzaIngredient(2L, "Сыр", 30);

        Pizza savedPizza = new Pizza(1L, "Маргарита", 300, 10, "cool", "img", Arrays.asList(ingredient1, ingredient2));
        when(pizzaMapper.toEntity(pizzaDTO)).thenReturn(savedPizza);
        when(pizzaRepository.save(any(Pizza.class))).thenReturn(savedPizza);
        when(pizzaMapper.toDTO(savedPizza)).thenReturn(pizzaDTO);

        PizzaDTO result = pizzaService.createPizza(pizzaDTO);

        assertNotNull(result);
        assertEquals(pizzaDTO.getName(), result.getName());
        assertEquals(pizzaDTO.getIngredients().size(), result.getIngredients().size());

        verify(pizzaRepository, times(1)).save(savedPizza);
        verify(pizzaMapper, times(1)).toDTO(savedPizza);
    }

    @Test
    void testUpdatePizza() {
        PizzaDTO updatedPizzaDTO = new PizzaDTO(1L, "Маргарита нью", 350, 12, "cool", "margherita.jpg", new ArrayList<>());
        Pizza existingPizza = new Pizza(1L, "Маргарита", 300, 10, "cool", "margherita.jpg", new ArrayList<>());

        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(existingPizza));
        when(pizzaMapper.partialUpdate(updatedPizzaDTO, existingPizza)).thenReturn(existingPizza);

        PizzaIngredient ingredient1 = new PizzaIngredient(1L, "Томаты", 20);
        PizzaIngredient ingredient2 = new PizzaIngredient(2L, "Сыр", 20);

        Pizza updatedPizza = new Pizza(1L, "Маргарита нью", 350, 12, "cool", "margherita.jpg", Arrays.asList(ingredient1, ingredient2));
        when(pizzaRepository.save(existingPizza)).thenReturn(updatedPizza);

        when(pizzaMapper.toDTO(updatedPizza)).thenReturn(updatedPizzaDTO);

        PizzaDTO result = pizzaService.updatePizza(1L, updatedPizzaDTO);

        assertNotNull(result);
        assertEquals(updatedPizzaDTO, result);

        verify(pizzaRepository, times(1)).findById(1L);
        verify(pizzaMapper, times(1)).partialUpdate(updatedPizzaDTO, existingPizza);
        verify(pizzaRepository, times(1)).save(existingPizza);
        verify(pizzaMapper, times(1)).toDTO(updatedPizza);
    }

    @Test
    void testUpdate_InvalidPizzaId() {
        PizzaDTO updatedPizzaDTO = new PizzaDTO(1L, "Маргарита нью", 350, 12, "cool", "margherita.jpg", new ArrayList<>());
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        PizzaDTO result = pizzaService.updatePizza(1L, updatedPizzaDTO);

        assertNull(result);

        verify(pizzaRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletePizza() {
        Long pizzaId = 1L;

        pizzaService.deletePizza(pizzaId);

        verify(pizzaRepository, times(1)).deleteById(pizzaId);
    }
}