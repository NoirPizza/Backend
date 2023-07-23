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

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CustomUserDetails loadById(Integer id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Cannot find user"));
        return CustomUserDetails.create(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginOrEmailOrPhonenumber(username, username, username);
        if (user == null) {
            throw new RuntimeException("Cannot find user with prompted login details: " + username);
        }
        return CustomUserDetails.create(user);
    }
}
