package com.pizza.pizzashop.services.PizzaIngredientService;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;

import java.util.List;

public interface PizzaIngredientService {
    List<PizzaIngredientDTO> getAllIngredients();
    PizzaIngredientDTO getIngredientById(Long id);
    PizzaIngredientDTO createIngredient(PizzaIngredientDTO ingredientDto);
    PizzaIngredientDTO updateIngredient(Long id, PizzaIngredientDTO ingredientDto);
    void deleteIngredient(Long id);
}
