package com.pizza.pizzashop.repositories;

import com.pizza.pizzashop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByNameAndSurname(String name, String surname);

    User findByLogin(String login);
    User findByLoginOrEmailOrPhonenumber(String login, String email, String phonenumber);
}