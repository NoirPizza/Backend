package com.pizza.pizzashop.services.AuthService;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.entities.User;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.mappers.UserMapper;
import com.pizza.pizzashop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * This class is an implementation of the AuthService interface that provides
 * methods for user authentication and user information retrieval.
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Attempts to sign in a user with the provided credentials.
     *
     * @param credentials The LoginDTO containing login credentials (username and password).
     * @return The UserDTO representing the signed-in user's information.
     * @throws AuthenticationFailedException If authentication with the provided credentials fails.
     */
    @Override
    public UserDTO signIn(LoginDTO credentials) throws AuthenticationFailedException {
        User user = userRepository.findByLoginOrEmailOrPhoneNumber(
                credentials.getLogin(),
                credentials.getEmail(),
                credentials.getPhoneNumber());
        if (user == null || !passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException("Wrong credentials. Try again");
        }
        return userMapper.toDto(user);
    }

    /**
     * Registers a new user using the provided registration form data.
     *
     * @param registrationForm The UserDTO containing the registration form data for the new user.
     * @throws AuthenticationFailedException If the sign-up process encounters an authentication failure.
     */
    @Override
    public void signUp(UserDTO registrationForm) throws AuthenticationFailedException {
        if (userRepository.findByLoginOrEmailOrPhoneNumber(
                registrationForm.getLogin(),
                registrationForm.getEmail(),
                registrationForm.getPhoneNumber()
        ) != null) {
            throw new AuthenticationFailedException("User with prompted credential already exists");
        }
        User newUser = userMapper.toEntity(registrationForm);
        newUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        newUser.setCreatedAt(new Date().toInstant());
        newUser.setUpdatedAt(new Date().toInstant());
        userRepository.save(newUser);
    }

    /**
     * Retrieves user information for the specified user ID.
     *
     * @param userId The ID of the user for whom to retrieve the information.
     * @return The UserDTO representing the user's information.
     * @throws AuthenticationFailedException If the user with the specified ID is not found or authentication fails.
     */
    @Override
    public UserDTO userInfo(Long userId) throws AuthenticationFailedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationFailedException("Couldn't find user with such ID"));
        return userMapper.toDto(user);
    }
}
