package com.pizza.pizzashop.security.CustomUserDetailsService;

import com.pizza.pizzashop.exceptions.UserNotFoundException;
import com.pizza.pizzashop.security.CustomUserDetails;

public interface CustomUserDetailsService {

    CustomUserDetails loadById(Integer id) throws  UserNotFoundException;
}
