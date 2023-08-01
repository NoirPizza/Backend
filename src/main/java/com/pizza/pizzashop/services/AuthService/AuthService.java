package com.pizza.pizzashop.services.AuthService;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;

/**
 * This interface defines methods for authentication and user information retrieval.
 * It provides functionality to sign in, sign up, and retrieve user information.
 */
public interface AuthService {
    /**
     * Attempts to sign in a user with the provided credentials.
     *
     * @param credentials The LoginDTO containing login credentials (username and password).
     * @return The UserDTO representing the signed-in user's information.
     * @throws AuthenticationFailedException If authentication with the provided credentials fails.
     */
    UserDTO signIn(LoginDTO credentials) throws AuthenticationFailedException;

    /**
     * Registers a new user using the provided registration form data.
     *
     * @param registrationForm The UserDTO containing the registration form data for the new user.
     * @throws AuthenticationFailedException If the sign-up process encounters an authentication failure.
     */
    void signUp(UserDTO registrationForm) throws AuthenticationFailedException;

    /**
     * Retrieves user information for the specified user ID.
     *
     * @param userId The ID of the user for whom to retrieve the information.
     * @return The UserDTO representing the user's information.
     * @throws AuthenticationFailedException If the user with the specified ID is not found or authentication fails.
     */
    UserDTO userInfo(Long userId) throws AuthenticationFailedException;
}
