package com.pizza.pizzashop.repositories;

import com.pizza.pizzashop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginOrEmailOrPhoneNumber(String login, String email, String phoneNumber);
}