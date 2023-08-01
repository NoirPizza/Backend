package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.PizzaIngredientService.PizzaIngredientService;
import com.pizza.pizzashop.utils.GlobalExceptionHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles pizza ingredient-related API endpoints for CRUD (Create, Read, Update, Delete) operations.
 * It provides methods to retrieve all pizza ingredients, fetch a pizza ingredient by its ID,
 * create a new pizza ingredient, update an existing pizza ingredient, and delete a pizza ingredient from the system.
 */
@Validated
@RestController
@RequestMapping("/api/ingredient")
public class PizzaIngredientController {
    private final PizzaIngredientService ingredientService;

    @Autowired
    public PizzaIngredientController(PizzaIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Handle HTTP GET requests to "/api/ingredient" for retrieving all pizza ingredients.
     *
     * @return A ResponseEntity containing a SuccessDTO with a list of PizzaIngredientDTO representing all pizza ingredients in the system.
     */
    @GetMapping
    public ResponseEntity<SuccessDTO<List<PizzaIngredientDTO>>> getAllIngredients() {
        List<PizzaIngredientDTO> ingredients = ingredientService.getAllIngredients();
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Pizza Ingredient Get All",
                        ingredients
                ), HttpStatus.OK);
    }

    /**
     * Handle HTTP GET requests to "/api/ingredient/{id}" for fetching a pizza ingredient by its ID.
     *
     * @param id The ID of the pizza ingredient to fetch.
     * @return A ResponseEntity containing a SuccessDTO with the PizzaIngredientDTO representing the requested pizza ingredient.
     * @throws NotFoundException If the pizza ingredient with the given ID is not found in the system.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessDTO<PizzaIngredientDTO>> getIngredientById(
            @Positive(message = " Ingredient ID must be positive") @PathVariable Long id
    ) throws NotFoundException {
        PizzaIngredientDTO ingredient = ingredientService.getIngredientById(id);
        if (ingredient != null) {
            return new ResponseEntity<>(
                    new SuccessDTO<>(
                            HttpStatus.OK.value(),
                            "Pizza Ingredient Get By Id",
                            ingredient
                    ), HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find ingredient with ID " + id);
    }

    /**
     * Handle HTTP POST requests to "/api/ingredient" for creating a new pizza ingredient.
     *
     * @param ingredientDTO    The PizzaIngredientDTO containing the details of the pizza ingredient to create.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @return A ResponseEntity containing a SuccessDTO with the newly created PizzaIngredientDTO.
     * @throws RequestDataValidationFailedException If there are validation errors in the ingredientDTO.
     */
    @PostMapping
    public ResponseEntity<SuccessDTO<PizzaIngredientDTO>> createIngredient(
            @Valid @RequestBody PizzaIngredientDTO ingredientDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        PizzaIngredientDTO createdIngredient = ingredientService.createIngredient(ingredientDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "Pizza Ingredient Create",
                        createdIngredient
                ), HttpStatus.CREATED);
    }

    /**
     * Handle HTTP PUT requests to "/api/ingredient/{id}" for updating an existing pizza ingredient.
     *
     * @param id               The ID of the pizza ingredient to update.
     * @param ingredientDTO    The updated PizzaIngredientDTO containing the new details of the pizza ingredient.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @return A ResponseEntity containing a SuccessDTO with the updated PizzaIngredientDTO.
     * @throws RequestDataValidationFailedException If there are validation errors in the ingredientDTO.
     * @throws NotFoundException         If the pizza ingredient with the given ID is not found in the system.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuccessDTO<PizzaIngredientDTO>> updatePizza(
            @Positive(message = " Ingredient ID must be positive") @PathVariable Long id,
            @Valid @RequestBody PizzaIngredientDTO ingredientDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        PizzaIngredientDTO updatedIngredient = ingredientService.updateIngredient(id, ingredientDTO);
        if (updatedIngredient != null) {
            return new ResponseEntity<>(
                    new SuccessDTO<>(
                            HttpStatus.OK.value(),
                            "Pizza Ingredient Update",
                            updatedIngredient
                    ), HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find ingredient with ID " + id);
    }

    /**
     * Handle HTTP DELETE requests to "/api/ingredient/{id}" for deleting a pizza ingredient.
     *
     * @param id The ID of the pizza ingredient to delete.
     * @return A ResponseEntity containing a SuccessDTO with a message indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO<String>> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Pizza Ingredient Delete",
                        "Successfully deleted pizza ingredient"
                ),
                HttpStatus.OK);
    }
}
