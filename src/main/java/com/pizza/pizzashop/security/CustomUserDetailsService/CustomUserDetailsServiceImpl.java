package com.pizza.pizzashop.security.CustomUserDetailsService;

import com.pizza.pizzashop.entities.User;
import com.pizza.pizzashop.exceptions.UserNotFoundException;
import com.pizza.pizzashop.repositories.UserRepository;
import com.pizza.pizzashop.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This is a service class that implements both the CustomUserDetailsService and UserDetailsService interfaces.
 * It is responsible for loading custom user details and user authentication details based on user IDs or usernames.
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads custom user details from custom implementation of spring.security.UserDetails based on the provided user ID.
     *
     * @param id The ID of the user for whom the details are to be loaded.
     * @return A CustomUserDetails object representing the user's custom details.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Override
    @Transactional
    public CustomUserDetails loadById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Cannot find user"));
        return CustomUserDetails.create(user);
    }

    /**
     * Loads user authentication details based on the provided username (login, email, or phone number).
     *
     * @param username The username (login, email, or phone number) of the user for whom the details are to be loaded.
     * @return A UserDetails object representing the user's authentication details.
     * @throws UsernameNotFoundException If the user with the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginOrEmailOrPhoneNumber(username, username, username);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find user with prompted login details: " + username);
        }
        return CustomUserDetails.create(user);
    }
}
