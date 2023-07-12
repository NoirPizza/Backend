package com.pizza.pizzashop.mappers;

import com.pizza.pizzashop.entities.Pizza;
import com.pizza.pizzashop.dtos.PizzaDTO;

import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PizzaMapper {

    PizzaDTO toDTO(Pizza pizza);

    Pizza toEntity(PizzaDTO pizzaDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Pizza partialUpdate(PizzaDTO pizzaIngredientDTO, @MappingTarget Pizza pizza);
}
