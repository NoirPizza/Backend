package com.pizza.pizzashop.services.PizzaService;

import com.pizza.pizzashop.dtos.PizzaDTO;
import com.pizza.pizzashop.entities.Pizza;
import com.pizza.pizzashop.mappers.PizzaMapper;
import com.pizza.pizzashop.repositories.PizzaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is an implementation of the PizzaService interface that provides methods for managing pizzas.
 */
@Service
public class PizzaServiceImpl implements PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;

    @Autowired
    public PizzaServiceImpl(PizzaRepository pizzaRepository, PizzaMapper pizzaMapper) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaMapper = pizzaMapper;
    }

    /**
     * Retrieves a list of all pizzas.
     *
     * @return A list of PizzaDTO representing all pizzas.
     */
    @Override
    public List<PizzaDTO> getAllPizzas() {
        List<Pizza> pizzas = pizzaRepository.findAll();
        return pizzas.stream()
                .map(pizzaMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a pizza by its unique identifier.
     *
     * @param id The unique identifier of the pizza to retrieve.
     * @return The PizzaDTO representing the pizza with the specified ID.
     */
    @Override
    public PizzaDTO getPizzaById(Long id) {
        Optional<Pizza> pizza = pizzaRepository.findById(id);
        return pizza.map(pizzaMapper::toDTO).orElse(null);
    }

    /**
     * Creates a new pizza.
     *
     * @param pizzaDTO The PizzaDTO containing information for the new pizza.
     * @return The PizzaDTO representing the newly created pizza.
     */
    @Override
    public PizzaDTO createPizza(PizzaDTO pizzaDTO) {
        Pizza pizza = pizzaMapper.toEntity(pizzaDTO);
        Pizza savedPizza = pizzaRepository.save(pizza);
        return pizzaMapper.toDTO(savedPizza);
    }

    /**
     * Updates an existing pizza with new information.
     *
     * @param id       The unique identifier of the pizza to update.
     * @param pizzaDTO The PizzaDTO containing updated information for the pizza.
     * @return The PizzaDTO representing the updated pizza.
     */
    @Override
    public PizzaDTO updatePizza(Long id, PizzaDTO pizzaDTO) {
        Optional<Pizza> existingPizza = pizzaRepository.findById(id);
        if (existingPizza.isPresent()) {
            Pizza pizza = pizzaMapper.partialUpdate(pizzaDTO, existingPizza.get());
            Pizza updatedPizza = pizzaRepository.save(pizza);
            return pizzaMapper.toDTO(updatedPizza);
        }
        return null;
    }

    /**
     * Deletes a pizza by its unique identifier.
     *
     * @param id The unique identifier of the pizza to delete.
     */
    @Override
    public void deletePizza(Long id) {
        pizzaRepository.deleteById(id);
    }
}
