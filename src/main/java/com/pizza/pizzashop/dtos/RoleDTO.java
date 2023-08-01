package com.pizza.pizzashop.dtos;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * The RoleDTO class represents a data transfer object (DTO) that encapsulates role information in a standardized format.
 * It is used to provide a consistent structure for passing role details, including its ID and name.
 */
public class RoleDTO implements Serializable {
    private final Long id;
    @NotNull
    private final String name;

    public RoleDTO(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO entity = (RoleDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}