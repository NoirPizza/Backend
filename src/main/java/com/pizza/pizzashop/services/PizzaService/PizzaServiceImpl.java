package com.pizza.pizzashop.services.PizzaService;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.entities.Pizza;
import com.pizza.pizzashop.entities.PizzaIngredient;
import com.pizza.pizzashop.mappers.PizzaMapper;
import com.pizza.pizzashop.repositories.PizzaIngredientRepository;
import com.pizza.pizzashop.repositories.PizzaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PizzaServiceImpl implements PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaIngredientRepository ingredientRepository;
    private final PizzaMapper pizzaMapper;

    @Autowired
    public PizzaServiceImpl(PizzaRepository pizzaRepository, PizzaIngredientRepository ingredientRepository, PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.ingredientRepository = ingredientRepository;
        this.pizzaMapper = pizzaMapper;
    }

    @Override
    public List<PizzaDTO> getAllPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        return pizzas.stream()
                .map(pizzaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PizzaDTO getPizzaById(Long id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        return pizza.map(pizzaMapper::toDTO).orElse(null);
    }

    @Override
    public PizzaDTO createPizza(PizzaDTO pizzaDTO) {
        Pizza pizza = pizzaMapper.toEntity(pizzaDTO);
        List<PizzaIngredient> ingredients = pizzaDTO.getIngredients().stream()
                .map(ingredientDto -> ingredientRepository.findById(ingredientDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Ingredient ID")))
                .toList();
        Pizza savedPizza = pizzaRepository.save(pizza);
        return pizzaMapper.toDTO(savedPizza);
    }

    @Override
    public PizzaDTO updatePizza(Long id, PizzaDTO pizzaDTO) {
        Optional<Pizza> existingPizza = pizzaRepository.findById(id);
        if (existingPizza.isPresent()) {
            Pizza pizza = existingPizza.get();
            pizza.setName(pizzaDTO.getName());
            pizza.setWeight(pizzaDTO.getWeight());
            pizza.setPrice(pizzaDTO.getPrice());
            pizza.setDescription(pizzaDTO.getDescription());
            pizza.setImage(pizzaDTO.getImage());
            List<PizzaIngredient> ingredients = pizzaDTO.getIngredients().stream()
                    .map(ingredientDto -> ingredientRepository.findById(ingredientDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid Ingredient ID")))
                    .collect(Collectors.toList());
            pizza.setIngredients(ingredients);
            Pizza updatedPizza = pizzaRepository.save(pizza);
            return pizzaMapper.toDTO(updatedPizza);
        }
        return null;
    }

    @Override
    public void deletePizza(Long id) {
        pizzaRepository.deleteById(id);
    }
}
