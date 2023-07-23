package com.pizza.pizzashop.dtos;

import com.pizza.pizzashop.entities.Pizza;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link Pizza}
 */
public class PizzaDTO implements Serializable {
    private Long id;
    @NotNull
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name length should be in range from 1 to 100")
    private String name;
    @NotNull
    @Min(value = 1, message = "Weight value must be greater than 0")
    private Integer weight;
    @NotNull
    @Min(value = 1, message = "Price value must be greater than 0")
    private Integer price;

    private String description;
    @NotNull
    @NotBlank(message = "Image is required in base64 format")
    private String image;
    @NotNull
    @Size(min = 1, message = "Must be at least 1 ingredient added")
    private List<PizzaIngredientDTO> ingredients;

    public PizzaDTO(Long id,
                    @NotNull String name,
                    @NotNull Integer weight,
                    @NotNull Integer price,
                    String description,
                    @NotNull String image,
                    @NotNull List<PizzaIngredientDTO> ingredients
    ) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
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

    public @NotNull Integer getWeight() {
        return weight;
    }

    public void setWeight(@NotNull Integer weight) {
        this.weight = weight;
    }

    public @NotNull Integer getPrice() {
        return price;
    }

    public void setPrice(@NotNull Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull String getImage() {
        return image;
    }

    public void setImage(@NotNull String image) {
        this.image = image;
    }

    public @NotNull List<PizzaIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(@NotNull List<PizzaIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaDTO dto = (PizzaDTO) o;
        return Objects.equals(this.id, dto.id) &&
                Objects.equals(this.name, dto.name) &&
                Objects.equals(this.price, dto.price) &&
                Objects.equals(this.weight, dto.weight) &&
                Objects.equals(this.description, dto.description) &&
                Objects.equals(this.image, dto.image) &&
                Objects.equals(this.ingredients, dto.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, price, description, image, ingredients);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "price = " + price + ", " +
                "weight = " + weight + ", " +
                "description = " + description + ", " +
                "image = " + image + ", " +
                "ingredients = " + ingredients + ")";
    }
}