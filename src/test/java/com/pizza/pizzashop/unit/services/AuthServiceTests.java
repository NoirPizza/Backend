package com.pizza.pizzashop.unit.services;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.entities.User;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.mappers.UserMapper;
import com.pizza.pizzashop.repositories.UserRepository;
import com.pizza.pizzashop.services.AuthService.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn() throws AuthenticationFailedException {
        User user = createUser(false);
        LoginDTO credentials = new LoginDTO("user123", null, null, "password123");
        when(userRepository.findByLoginOrEmailOrPhoneNumber("user123", null, null)).thenReturn(user);
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        UserDTO userDTO = createUserDTO(false);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = authService.signIn(credentials);

        assertNotNull(result);
        assertEquals(userDTO, result);

        verify(userRepository, times(1)).findByLoginOrEmailOrPhoneNumber("user123", null, null);
        verify(passwordEncoder, times(1)).matches("password123", user.getPassword());
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testSignIn_FailedAuthentication() {
        User user = createUser(false);
        LoginDTO credentials = new LoginDTO("user123", null, null, "password123");

        when(userRepository.findByLoginOrEmailOrPhoneNumber("user123", null, null)).thenReturn(user);
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationFailedException.class, () -> authService.signIn(credentials));

        verify(userRepository, times(1)).findByLoginOrEmailOrPhoneNumber("user123", null, null);
        verify(passwordEncoder, times(1)).matches("password123", user.getPassword());
    }

    @Test
    void testSignUp() throws AuthenticationFailedException {
        UserDTO registrationForm = createUserDTO(false);

        when(userRepository.findByLoginOrEmailOrPhoneNumber("user123", "user@example.com", "+79999999999")).thenReturn(null);
        String encryptedPassword = "encryptedPassword";
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);

        User newUser = createUser(true);
        when(userMapper.toEntity(registrationForm)).thenReturn(newUser);
        User savedUser = createUser(false);
        when(userRepository.save(newUser)).thenReturn(savedUser);

        authService.signUp(registrationForm);

        verify(userRepository, times(1)).findByLoginOrEmailOrPhoneNumber("user123", "user@example.com", "+79999999999");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userMapper, times(1)).toEntity(registrationForm);
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testSignUp_DuplicateUser() {
        UserDTO registrationForm = createUserDTO(true);

        User existingUser = createUser(false);
        when(userRepository.findByLoginOrEmailOrPhoneNumber("user123", "user@example.com", "+79999999999")).thenReturn(existingUser);

        assertThrows(AuthenticationFailedException.class, () -> authService.signUp(registrationForm));

        verify(userRepository, times(1)).findByLoginOrEmailOrPhoneNumber("user123", "user@example.com", "+79999999999");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUserInfo() throws AuthenticationFailedException {
        User user = createUser(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDTO userDTO = createUserDTO(false);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = authService.userInfo(1L);

        assertNotNull(result);
        assertEquals(userDTO, result);

        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testUserInfo_NonExistingUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AuthenticationFailedException.class, () -> authService.userInfo(999L));

        verify(userRepository, times(1)).findById(999L);
    }

    private User createUser(boolean nullId) {
        return new User(
                nullId ? null : 1L,
                "user123",
                "password123",
                "John",
                "Doe",
                "+79999999999",
                "user@example.com",
                "01.01.2000",
                new Date().toInstant(),
                new Date().toInstant()
        );
    }

    private UserDTO createUserDTO(boolean nullId) {
        return new UserDTO(
                nullId ? null : 1L,
                "John",
                "Doe",
                "user123",
                "password123",
                "user@example.com",
                "+79999999999",
                "01.01.2000",
                new ArrayList<>()
        );
    }
}
