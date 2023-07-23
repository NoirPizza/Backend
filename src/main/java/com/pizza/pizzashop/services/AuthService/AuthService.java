package com.pizza.pizzashop.services.AuthService;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;

public interface AuthService {
    UserDTO signIn(LoginDTO credentials) throws AuthenticationFailedException;
    void signUp(UserDTO registrationForm) throws AuthenticationFailedException;

    UserDTO userInfo(Integer userId) throws AuthenticationFailedException;
}
