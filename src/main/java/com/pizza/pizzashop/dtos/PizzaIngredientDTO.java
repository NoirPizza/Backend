package com.pizza.pizzashop.dtos;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * The PizzaIngredientDTO class represents a data transfer object (DTO) that encapsulates pizza ingredient information
 * in a standardized format. It is used to provide a consistent structure for passing pizza ingredient details,
 * including its ID, name, and additional price.
 */
public class PizzaIngredientDTO implements Serializable {
    private Long id;
    @NotNull
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 40, message = "Name length should be in range from 1 to 40")
    private String name;
    @NotNull
    @Min(value = 1, message = "AddPrice value must be greater than 0")
    private Integer addPrice;

    public PizzaIngredientDTO(Long id, @NotNull String name, @NotNull Integer addPrice) {
        this.id = id;
        this.name = name;
        this.addPrice = addPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Integer getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(@NotNull Integer addPrice) {
        this.addPrice = addPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaIngredientDTO dto = (PizzaIngredientDTO) o;
        return Objects.equals(this.id, dto.id) &&
                Objects.equals(this.name, dto.name) &&
                Objects.equals(this.addPrice, dto.addPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addPrice);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "addPrice = " + addPrice + ")";
    }
}