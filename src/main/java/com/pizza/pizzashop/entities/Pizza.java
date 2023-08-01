package com.pizza.pizzashop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

/**
 * This class represents a pizza entity in the system.
 * It is annotated with JPA annotations to map the class to the corresponding database table.
 */
@Entity
@Table(name = "pizza")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ingredient_on_pizza",
            joinColumns = @JoinColumn(name = "pizzaid"),
            inverseJoinColumns = @JoinColumn(name = "ingredientid"))
    private List<PizzaIngredient> ingredients;

    public Pizza(
            Long id,
            String name,
            Integer weight,
            Integer price,
            String description,
            String image,
            List<PizzaIngredient> ingredients
    ) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
    }

    public Pizza() {

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

    public List<PizzaIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<PizzaIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
