package com.pizza.pizzashop.mappers;

import com.pizza.pizzashop.dtos.PizzaIngredientDTO;
import com.pizza.pizzashop.entities.PizzaIngredient;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PizzaIngredientMapper {
    PizzaIngredient toEntity(PizzaIngredientDTO pizzaIngredientDTO);

    PizzaIngredientDTO toDto(PizzaIngredient pizzaIngredient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PizzaIngredient partialUpdate(PizzaIngredientDTO pizzaIngredientDTO, @MappingTarget PizzaIngredient pizzaIngredient);
}