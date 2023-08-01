package com.pizza.pizzashop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * This class represents an ingredient entity used in pizzas in the system.
 * It is annotated with JPA annotations to map the class to the corresponding database table.
 */
@Entity
@Table(name = "pizza_ingredient")
public class PizzaIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "addprice", nullable = false)
    private Integer addPrice;

    public PizzaIngredient(Long id, String name, Integer addPrice) {
        this.id = id;
        this.name = name;
        this.addPrice = addPrice;
    }

    public PizzaIngredient() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(Integer addPrice) {
        this.addPrice = addPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
