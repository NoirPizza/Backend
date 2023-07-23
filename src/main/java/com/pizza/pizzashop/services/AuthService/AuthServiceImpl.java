package com.pizza.pizzashop.services.AuthService;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.entities.User;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.mappers.UserMapper;
import com.pizza.pizzashop.repositories.RoleRepository;
import com.pizza.pizzashop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO signIn(LoginDTO credentials) throws AuthenticationFailedException {
        User user = userRepository.findByLoginOrEmailOrPhonenumber(
                credentials.getLogin(),
                credentials.getEmail(),
                credentials.getPhonenumber());
        if (user == null || !passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new AuthenticationFailedException("Wrong credentials. Try again");
        }
        return userMapper.toDto(user);
    }

    @Override
    public void signUp(UserDTO registrationForm) throws AuthenticationFailedException {
        if (userRepository.findByLoginOrEmailOrPhonenumber(
                registrationForm.getLogin(),
                registrationForm.getEmail(),
                registrationForm.getPhonenumber()
        ) != null) {
            throw new AuthenticationFailedException("User with prompted credential already exists");
        }
        User newUser = userMapper.toEntity(registrationForm);
        newUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        newUser.setCreatedAt(new Date().toInstant());
        newUser.setUpdatedAt(new Date().toInstant());
        userRepository.save(newUser);
    }

    @Override
    public UserDTO userInfo(Integer userId) throws AuthenticationFailedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationFailedException("Couldn't find user with such ID"));
        return userMapper.toDto(user);
    }
}
