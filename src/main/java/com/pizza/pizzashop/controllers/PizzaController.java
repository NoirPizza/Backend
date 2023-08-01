package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.NotFoundException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.PizzaService.PizzaService;
import com.pizza.pizzashop.utils.GlobalExceptionHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles pizza-related API endpoints for CRUD (Create, Read, Update, Delete) operations.
 * It provides methods to retrieve all pizzas, fetch a pizza by its ID, create a new pizza, update an existing pizza,
 * and delete a pizza from the system.
 */
@RestController
@RequestMapping("/api/pizza")
public class PizzaController {
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    /**
     * Handle HTTP GET requests to "/api/pizza" for retrieving all pizzas.
     *
     * @return A ResponseEntity containing a SuccessDTO with a list of PizzaDTO representing all pizzas in the system.
     */
    @GetMapping
    public ResponseEntity<SuccessDTO<List<PizzaDTO>>> getAllPizzas() {
        List<PizzaDTO> pizzas = pizzaService.getAllPizzas();
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Pizza Get All",
                        pizzas
                ), HttpStatus.OK);
    }

    /**
     * Handle HTTP GET requests to "/api/pizza/{id}" for fetching a pizza by its ID.
     *
     * @param id The ID of the pizza to fetch.
     * @return A ResponseEntity containing a SuccessDTO with the PizzaDTO representing the requested pizza.
     * @throws NotFoundException If the pizza with the given ID is not found in the system.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessDTO<PizzaDTO>> getPizzaById(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id
    ) throws NotFoundException {
        PizzaDTO pizza = pizzaService.getPizzaById(id);
        if (pizza != null) {
            return new ResponseEntity<>(
                    new SuccessDTO<>(
                            HttpStatus.OK.value(),
                            "Pizza Get By Id",
                            pizza
                    ), HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find pizza with id " + id);
    }

    /**
     * Handle HTTP POST requests to "/api/pizza" for creating a new pizza.
     *
     * @param pizzaDTO         The PizzaDTO containing the details of the pizza to create.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @return A ResponseEntity containing a SuccessDTO with the newly created PizzaDTO.
     * @throws RequestDataValidationFailedException If there are validation errors in the pizzaDTO.
     */
    @PostMapping
    public ResponseEntity<SuccessDTO<PizzaDTO>> createPizza(
            @Valid @RequestBody PizzaDTO pizzaDTO, BindingResult validationResult
    ) throws RequestDataValidationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        PizzaDTO createdPizza = pizzaService.createPizza(pizzaDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "Pizza Create",
                        createdPizza
                ), HttpStatus.CREATED);
    }

    /**
     * Handle HTTP PUT requests to "/api/pizza/{id}" for updating an existing pizza.
     *
     * @param id               The ID of the pizza to update.
     * @param pizzaDTO         The updated PizzaDTO containing the new details of the pizza.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @return A ResponseEntity containing a SuccessDTO with the updated PizzaDTO.
     * @throws RequestDataValidationFailedException If there are validation errors in the pizzaDTO.
     * @throws NotFoundException         If the pizza with the given ID is not found in the system.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SuccessDTO<PizzaDTO>> updatePizza(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id,
            @Valid @RequestBody PizzaDTO pizzaDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, NotFoundException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        PizzaDTO updatedPizza = pizzaService.updatePizza(id, pizzaDTO);
        if (updatedPizza != null) {
            return new ResponseEntity<>(
                    new SuccessDTO<>(
                            HttpStatus.OK.value(),
                            "Pizza Update",
                            updatedPizza
                    ), HttpStatus.OK);
        }
        throw new NotFoundException("Unable to find pizza with id " + id);
    }

    /**
     * Handle HTTP DELETE requests to "/api/pizza/{id}" for deleting a pizza.
     *
     * @param id The ID of the pizza to delete.
     * @return A ResponseEntity containing a SuccessDTO with a message indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO<String>> deletePizza(
            @Positive(message = " Pizza ID must be positive") @PathVariable Long id
    ) {
        pizzaService.deletePizza(id);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Pizza Delete",
                        "Successfully deleted pizza"
                ),
                HttpStatus.OK);
    }
}
