package com.pizza.pizzashop.dtos;

import java.util.List;
import java.util.Objects;

public class PizzaDTO {
    private Long id;
    private String name;
    private Integer weight;
    private Integer price;
    private String description;
    private String image;

    private List<PizzaIngredientDTO> ingredients;

    public PizzaDTO(Long id, String name, Integer weight, Integer price, String description, String image, List<PizzaIngredientDTO> ingredients) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PizzaIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<PizzaIngredientDTO> ingredients) {
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
                "ingredients = " + ingredients.toString() + ")";
    }
}