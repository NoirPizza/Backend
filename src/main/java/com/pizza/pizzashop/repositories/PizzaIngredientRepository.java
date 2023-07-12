package com.pizza.pizzashop.repositories;

import com.pizza.pizzashop.entities.PizzaIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaIngredientRepository extends JpaRepository<PizzaIngredient, Long> {
}