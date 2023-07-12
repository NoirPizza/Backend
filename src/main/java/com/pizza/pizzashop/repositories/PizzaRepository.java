package com.pizza.pizzashop.repositories;

import com.pizza.pizzashop.entities.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {
}