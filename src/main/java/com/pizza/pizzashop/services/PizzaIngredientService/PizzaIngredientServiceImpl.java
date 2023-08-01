package com.pizza.pizzashop.services.PizzaIngredientService;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.entities.PizzaIngredient;
import com.pizza.pizzashop.mappers.PizzaIngredientMapper;
import com.pizza.pizzashop.repositories.PizzaIngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is an implementation of the PizzaIngredientService interface that provides methods for managing pizza ingredients.
 */
@Service
public class PizzaIngredientServiceImpl implements PizzaIngredientService {
    private final PizzaIngredientRepository ingredientRepository;
    private final PizzaIngredientMapper ingredientMapper;

    @Autowired
    public PizzaIngredientServiceImpl(
            PizzaIngredientRepository ingredientRepository,
            PizzaIngredientMapper ingredientMapper
    ) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * Retrieves a list of all pizza ingredients.
     *
     * @return A list of PizzaIngredientDTO representing all pizza ingredients.
     */
    @Override
    public List<PizzaIngredientDTO> getAllIngredients() {
        List<PizzaIngredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(ingredientMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a pizza ingredient by its unique identifier.
     *
     * @param id The unique identifier of the pizza ingredient to retrieve.
     * @return The PizzaIngredientDTO representing the pizza ingredient with the specified ID.
     */
    @Override
    public PizzaIngredientDTO getIngredientById(Long id) {
        Optional<PizzaIngredient> ingredient = ingredientRepository.findById(id);
        return ingredient.map(ingredientMapper::toDto).orElse(null);
    }

    /**
     * Creates a new pizza ingredient.
     *
     * @param ingredientDto The PizzaIngredientDTO containing information for the new pizza ingredient.
     * @return The PizzaIngredientDTO representing the newly created pizza ingredient.
     */
    @Override
    public PizzaIngredientDTO createIngredient(PizzaIngredientDTO ingredientDto) {
        PizzaIngredient ingredient = ingredientMapper.toEntity(ingredientDto);
        PizzaIngredient savedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    /**
     * Updates an existing pizza ingredient with new information.
     *
     * @param id            The unique identifier of the pizza ingredient to update.
     * @param ingredientDto The PizzaIngredientDTO containing updated information for the pizza ingredient.
     * @return The PizzaIngredientDTO representing the updated pizza ingredient.
     */
    @Override
    public PizzaIngredientDTO updateIngredient(Long id, PizzaIngredientDTO ingredientDto) {
        Optional<PizzaIngredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            PizzaIngredient ingredient = ingredientMapper.partialUpdate(ingredientDto, existingIngredient.get());
            PizzaIngredient updatedIngredient = ingredientRepository.save(ingredient);
            return ingredientMapper.toDto(updatedIngredient);
        }
        return null;
    }

    /**
     * Deletes a pizza ingredient by its unique identifier.
     *
     * @param id The unique identifier of the pizza ingredient to delete.
     */
    @Override
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
