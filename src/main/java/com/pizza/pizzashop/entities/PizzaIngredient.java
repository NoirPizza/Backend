package com.pizza.pizzashop.entities;

import jakarta.persistence.*;

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
    private Integer addprice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAddprice() {
        return addprice;
    }

    public void setAddprice(Integer addprice) {
        this.addprice = addprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
