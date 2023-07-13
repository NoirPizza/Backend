package com.pizza.pizzashop.dtos;

import com.pizza.pizzashop.entities.PizzaIngredient;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link PizzaIngredient}
 */
public class PizzaIngredientDTO implements Serializable {
    private Long id;
    @NotNull
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 40, message = "Name length should be in range from 1 to 40")
    private String name;
    @NotNull
    @Min(value = 1, message = "Addrice value must be greater than 0")
    private Integer addprice;

    public PizzaIngredientDTO(Long id, @NotNull String name, @NotNull Integer addprice) {
        this.id = id;
        this.name = name;
        this.addprice = addprice;
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

    public @NotNull Integer getAddprice() {
        return addprice;
    }

    public void setAddprice(@NotNull Integer addprice) {
        this.addprice = addprice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaIngredientDTO dto = (PizzaIngredientDTO) o;
        return Objects.equals(this.id, dto.id) &&
                Objects.equals(this.name, dto.name) &&
                Objects.equals(this.addprice, dto.addprice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addprice);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "addprice = " + addprice + ")";
    }
}