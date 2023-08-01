package com.pizza.pizzashop.services.PizzaIngredientService;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;

import java.util.List;

/**
 * This interface provides methods for managing pizza ingredients.
 */
public interface PizzaIngredientService {
    /**
     * Retrieves a list of all pizza ingredients.
     *
     * @return A list of PizzaIngredientDTO representing all pizza ingredients.
     */
    List<PizzaIngredientDTO> getAllIngredients();

    /**
     * Retrieves a pizza ingredient by its unique identifier.
     *
     * @param id The unique identifier of the pizza ingredient to retrieve.
     * @return The PizzaIngredientDTO representing the pizza ingredient with the specified ID.
     */
    PizzaIngredientDTO getIngredientById(Long id);

    /**
     * Creates a new pizza ingredient.
     *
     * @param ingredientDto The PizzaIngredientDTO containing information for the new pizza ingredient.
     * @return The PizzaIngredientDTO representing the newly created pizza ingredient.
     */
    PizzaIngredientDTO createIngredient(PizzaIngredientDTO ingredientDto);

    /**
     * Updates an existing pizza ingredient with new information.
     *
     * @param id            The unique identifier of the pizza ingredient to update.
     * @param ingredientDto The PizzaIngredientDTO containing updated information for the pizza ingredient.
     * @return The PizzaIngredientDTO representing the updated pizza ingredient.
     */
    PizzaIngredientDTO updateIngredient(Long id, PizzaIngredientDTO ingredientDto);

    /**
     * Deletes a pizza ingredient by its unique identifier.
     *
     * @param id The unique identifier of the pizza ingredient to delete.
     */
    void deleteIngredient(Long id);
}
