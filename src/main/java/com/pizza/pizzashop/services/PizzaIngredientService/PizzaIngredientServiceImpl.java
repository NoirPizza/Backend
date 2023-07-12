package com.pizza.pizzashop.services.PizzaIngredientService;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.entities.PizzaIngredient;
import com.pizza.pizzashop.mappers.PizzaIngredientMapper;
import com.pizza.pizzashop.repositories.PizzaIngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PizzaIngredientServiceImpl implements PizzaIngredientService {
    private final PizzaIngredientRepository ingredientRepository;
    private final PizzaIngredientMapper ingredientMapper;

    @Autowired
    public PizzaIngredientServiceImpl(PizzaIngredientRepository ingredientRepository, PizzaIngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<PizzaIngredientDTO> getAllIngredients() {
        List<PizzaIngredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(ingredientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PizzaIngredientDTO getIngredientById(Long id) {
        Optional<PizzaIngredient> ingredient = ingredientRepository.findById(id);
        return ingredient.map(ingredientMapper::toDto).orElse(null);
    }

    @Override
    public PizzaIngredientDTO createIngredient(PizzaIngredientDTO ingredientDto) {
        PizzaIngredient ingredient = ingredientMapper.toEntity(ingredientDto);
        PizzaIngredient savedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    @Override
    public PizzaIngredientDTO updateIngredient(Long id, PizzaIngredientDTO ingredientDto) {
        Optional<PizzaIngredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            PizzaIngredient ingredient = existingIngredient.get();
            ingredient.setName(ingredientDto.getName());
            ingredient.setAddprice(ingredientDto.getAddprice());
            PizzaIngredient updatedIngredient = ingredientRepository.save(ingredient);
            return ingredientMapper.toDto(updatedIngredient);
        }
        return null;
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
