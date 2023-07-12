package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.services.PizzaIngredientService.PizzaIngredientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
public class PizzaIngredientController {
    private final PizzaIngredientService ingredientService;

    @Autowired
    public PizzaIngredientController(PizzaIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<PizzaIngredientDTO>> getAllIngredients() {
        List<PizzaIngredientDTO> ingredients = ingredientService.getAllIngredients();
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaIngredientDTO> getIngredientById(@PathVariable Long id) {
        PizzaIngredientDTO ingredient = ingredientService.getIngredientById(id);
        if (ingredient != null) {
            return new ResponseEntity<>(ingredient, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<PizzaIngredientDTO> createIngredient(@RequestBody PizzaIngredientDTO ingredientDTO) {
        PizzaIngredientDTO createdIngredient = ingredientService.createIngredient(ingredientDTO);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaIngredientDTO> updatePizza(
            @PathVariable Long id,
            @RequestBody PizzaIngredientDTO ingredientDTO
    ) {
        PizzaIngredientDTO updatedIngredient = ingredientService.updateIngredient(id, ingredientDTO);
        if (updatedIngredient != null) {
            return new ResponseEntity<>(updatedIngredient, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
