package com.pizza.pizzashop.services.PizzaService;

import com.pizza.pizzashop.dtos.PizzaDTO;

import java.util.List;

/**
 * This interface provides methods for managing pizzas.
 */
public interface PizzaService {
    /**
     * Retrieves a list of all pizzas.
     *
     * @return A list of PizzaDTO representing all pizzas.
     */
    List<PizzaDTO> getAllPizzas();

    /**
     * Retrieves a pizza by its unique identifier.
     *
     * @param id The unique identifier of the pizza to retrieve.
     * @return The PizzaDTO representing the pizza with the specified ID.
     */
    PizzaDTO getPizzaById(Long id);

    /**
     * Creates a new pizza.
     *
     * @param pizzaDTO The PizzaDTO containing information for the new pizza.
     * @return The PizzaDTO representing the newly created pizza.
     */
    PizzaDTO createPizza(PizzaDTO pizzaDTO);

    /**
     * Updates an existing pizza with new information.
     *
     * @param id       The unique identifier of the pizza to update.
     * @param pizzaDTO The PizzaDTO containing updated information for the pizza.
     * @return The PizzaDTO representing the updated pizza.
     */
    PizzaDTO updatePizza(Long id, PizzaDTO pizzaDTO);

    /**
     * Deletes a pizza by its unique identifier.
     *
     * @param id The unique identifier of the pizza to delete.
     */
    void deletePizza(Long id);
}
