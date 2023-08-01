package com.pizza.pizzashop.repositories;

import com.pizza.pizzashop.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}