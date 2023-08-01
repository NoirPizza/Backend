package com.pizza.pizzashop.unit.services;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.entities.PizzaIngredient;
import com.pizza.pizzashop.mappers.PizzaIngredientMapper;
import com.pizza.pizzashop.repositories.PizzaIngredientRepository;
import com.pizza.pizzashop.services.PizzaIngredientService.PizzaIngredientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PizzaIngredientServiceTests {
    @Mock
    private PizzaIngredientRepository ingredientRepository;
    @Mock
    private PizzaIngredientMapper ingredientMapper;
    @InjectMocks
    private PizzaIngredientServiceImpl pizzaIngredientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllIngredients() {
        PizzaIngredient ingredient1 = new PizzaIngredient(1L, "Томаты", 20);
        PizzaIngredient ingredient2 = new PizzaIngredient(2L, "Сыр", 30);
        List<PizzaIngredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        PizzaIngredientDTO ingredientDTO1 = new PizzaIngredientDTO(1L, "Томаты", 20);
        PizzaIngredientDTO ingredientDTO2 = new PizzaIngredientDTO(2L, "Сыр", 30);
        when(ingredientMapper.toDto(ingredient1)).thenReturn(ingredientDTO1);
        when(ingredientMapper.toDto(ingredient2)).thenReturn(ingredientDTO2);

        List<PizzaIngredientDTO> result = pizzaIngredientService.getAllIngredients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ingredientDTO1, result.get(0));
        assertEquals(ingredientDTO2, result.get(1));

        verify(ingredientRepository, times(1)).findAll();
        verify(ingredientMapper, times(1)).toDto(ingredient1);
        verify(ingredientMapper, times(1)).toDto(ingredient2);
    }

    @Test
    void testGetIngredientById() {
        PizzaIngredient ingredient = new PizzaIngredient(1L, "Томаты", 20);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
        
        PizzaIngredientDTO ingredientDTO = new PizzaIngredientDTO(1L, "Томаты", 20);
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);
        
        PizzaIngredientDTO result = pizzaIngredientService.getIngredientById(1L);
        
        assertNotNull(result);
        assertEquals(ingredientDTO, result);
        
        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientMapper, times(1)).toDto(ingredient);
    }

    @Test
    void testGetIngredientById_NonExistingIngredient() {
        when(ingredientRepository.findById(999L)).thenReturn(Optional.empty());
        
        PizzaIngredientDTO result = pizzaIngredientService.getIngredientById(999L);

        assertNull(result);

        verify(ingredientRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateIngredient() {
        PizzaIngredientDTO ingredientDTO = new PizzaIngredientDTO(1L, "Томаты", 20);
        PizzaIngredient ingredient = new PizzaIngredient(1L, "Томаты", 20);
        when(ingredientMapper.toEntity(ingredientDTO)).thenReturn(ingredient);

        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
        
        when(ingredientMapper.toDto(ingredient)).thenReturn(ingredientDTO);
        
        PizzaIngredientDTO result = pizzaIngredientService.createIngredient(ingredientDTO);
        
        assertNotNull(result);
        assertEquals(ingredientDTO, result);
        
        verify(ingredientMapper, times(1)).toEntity(ingredientDTO);
        verify(ingredientRepository, times(1)).save(ingredient);
        verify(ingredientMapper, times(1)).toDto(ingredient);
    }

    @Test
    void testUpdateIngredient() {
        PizzaIngredientDTO updatedIngredientDTO = new PizzaIngredientDTO(1L, "Томаты New", 25);
        PizzaIngredient existingIngredient = new PizzaIngredient(1L, "Томаты", 20);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(existingIngredient));
        
        PizzaIngredient updatedIngredient = new PizzaIngredient(1L, "Томаты New", 25);
        when(ingredientMapper.partialUpdate(updatedIngredientDTO, existingIngredient)).thenReturn(updatedIngredient);
        
        when(ingredientRepository.save(updatedIngredient)).thenReturn(updatedIngredient);
        when(ingredientMapper.toDto(updatedIngredient)).thenReturn(updatedIngredientDTO);
        
        PizzaIngredientDTO result = pizzaIngredientService.updateIngredient(1L, updatedIngredientDTO);
        
        assertNotNull(result);
        assertEquals(updatedIngredientDTO, result);
        
        verify(ingredientRepository, times(1)).findById(1L);
        verify(ingredientMapper, times(1)).partialUpdate(updatedIngredientDTO, existingIngredient);
        verify(ingredientRepository, times(1)).save(updatedIngredient);
        verify(ingredientMapper, times(1)).toDto(updatedIngredient);
    }

    @Test
    void testUpdateIngredient_NonExistingIngredient() {
        PizzaIngredientDTO updatedIngredientDTO = new PizzaIngredientDTO(999L, "Томаты New", 25);
        when(ingredientRepository.findById(999L)).thenReturn(Optional.empty());

        PizzaIngredientDTO result = pizzaIngredientService.updateIngredient(999L, updatedIngredientDTO);

        assertNull(result);

        verify(ingredientRepository, times(1)).findById(999L);
    }

    @Test
    void testDeleteIngredient() {
        pizzaIngredientService.deleteIngredient(1L);
        
        verify(ingredientRepository, times(1)).deleteById(1L);
    }
}
