package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.services.PizzaService.PizzaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizza")
public class PizzaController {
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    // TODO: think about pagination and various filters
    @GetMapping
    public ResponseEntity<List<PizzaDTO>> getAllPizzas() {
        List<PizzaDTO> pizzas = pizzaService.getAllPizzas();
        return new ResponseEntity<>(pizzas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(@PathVariable Long id) {
        PizzaDTO pizza = pizzaService.getPizzaById(id);
        if (pizza != null) {
            return new ResponseEntity<>(pizza, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody PizzaDTO PizzaDTO) {
        PizzaDTO createdPizza = pizzaService.createPizza(PizzaDTO);
        return new ResponseEntity<>(createdPizza, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(
            @PathVariable Long id,
            @RequestBody PizzaDTO PizzaDTO
    ) {
        PizzaDTO updatedPizza = pizzaService.updatePizza(id, PizzaDTO);
        if (updatedPizza != null) {
            return new ResponseEntity<>(updatedPizza, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        pizzaService.deletePizza(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
