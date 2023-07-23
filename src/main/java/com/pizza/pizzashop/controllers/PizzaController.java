package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.ValidationFailedException;
import com.pizza.pizzashop.services.PizzaService.PizzaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/pizza")
public class PizzaController {
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<List<PizzaDTO>> getAllPizzas() {
        List<PizzaDTO> pizzas = pizzaService.getAllPizzas();
        return new ResponseEntity<>(pizzas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id
    ) throws NotFoundException {
        PizzaDTO pizza = pizzaService.getPizzaById(id);
        if (pizza != null) {
            return new ResponseEntity<>(pizza, HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find pizza with id " + id);
    }

    @PostMapping
    public ResponseEntity<PizzaDTO> createPizza(@Valid @RequestBody PizzaDTO PizzaDTO, BindingResult result) throws ValidationFailedException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        PizzaDTO createdPizza = pizzaService.createPizza(PizzaDTO);
        return new ResponseEntity<>(createdPizza, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id,
            @Valid @RequestBody PizzaDTO PizzaDTO,
            BindingResult result
    ) throws ValidationFailedException, NotFoundException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        PizzaDTO updatedPizza = pizzaService.updatePizza(id, PizzaDTO);
        if (updatedPizza != null) {
            return new ResponseEntity<>(updatedPizza, HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find pizza with id " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id
    ) {
        pizzaService.deletePizza(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
