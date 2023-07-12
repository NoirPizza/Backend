package com.pizza.pizzashop.services.PizzaService;

import com.pizza.pizzashop.dtos.PizzaDTO;

import java.util.List;

public interface PizzaService {
    List<PizzaDTO> getAllPizzas();

    PizzaDTO getPizzaById(Long id);

    PizzaDTO createPizza(PizzaDTO pizzaDTO);

    PizzaDTO updatePizza(Long id, PizzaDTO pizzaDTO);

    void deletePizza(Long id);
}
