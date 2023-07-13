package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.ErrorDTO;
import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.ValidationFailedException;
import com.pizza.pizzashop.services.PizzaIngredientService.PizzaIngredientService;

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
    public ResponseEntity<PizzaIngredientDTO> getIngredientById(
            @Positive(message = " Ingredient ID must be positive") @PathVariable Long id
    ) throws NotFoundException {
        PizzaIngredientDTO ingredient = ingredientService.getIngredientById(id);
        if (ingredient != null) {
            return new ResponseEntity<>(ingredient, HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find ingredient with id " + id);
    }

    @PostMapping
    public ResponseEntity<PizzaIngredientDTO> createIngredient(
            @Valid @RequestBody PizzaIngredientDTO ingredientDTO,
            BindingResult result
    ) throws ValidationFailedException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        PizzaIngredientDTO createdIngredient = ingredientService.createIngredient(ingredientDTO);
        return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaIngredientDTO> updatePizza(
            @Positive(message = " Ingredient ID must be positive") @PathVariable Long id,
            @Valid @RequestBody PizzaIngredientDTO ingredientDTO,
            BindingResult result
    ) throws ValidationFailedException, NotFoundException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        PizzaIngredientDTO updatedIngredient = ingredientService.updateIngredient(id, ingredientDTO);
        if (updatedIngredient != null) {
            return new ResponseEntity<>(updatedIngredient, HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find ingredient with id " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
