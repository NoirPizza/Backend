package com.pizza.pizzashop.security.CustomUserDetailsService;

import com.pizza.pizzashop.exceptions.UserNotFoundException;
import com.pizza.pizzashop.security.CustomUserDetails;

/**
 * This interface defines a contract for loading custom user details based on their ID.
 */
public interface CustomUserDetailsService {

    /**
     * Loads custom user details from custom implementation of spring.security.UserDetails based on the provided user ID.
     *
     * @param id The ID of the user for whom the details are to be loaded.
     * @return A CustomUserDetails object representing the user's custom details.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    CustomUserDetails loadById(Long id) throws  UserNotFoundException;
}
