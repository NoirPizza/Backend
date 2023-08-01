package com.pizza.pizzashop.unit.controllers;

import com.pizza.pizzashop.controllers.AuthController;
import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.AuthService.AuthService;
import com.pizza.pizzashop.utils.JWTHelper;
import com.pizza.pizzashop.utils.RedisHelper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTests {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTHelper jwtHelper;
    @Mock
    private RedisHelper redisHelper;
    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;
    @Mock
    private BindingResult bindingResult;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testSignIn() throws RequestDataValidationFailedException, AuthenticationFailedException {
        LoginDTO loginDTO = createLoginDTO();
        UserDTO userDTO = createUserDTO();
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authService.signIn(loginDTO)).thenReturn(userDTO);
        String accessToken = "generated_access_token";
        when(jwtHelper.generateToken(authentication)).thenReturn(accessToken);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<UserDTO>> responseEntity = authController.singIn(loginDTO, bindingResult, response);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDTO, responseEntity.getBody().getData());
        verify(response, times(1)).addCookie(any(Cookie.class));
        verify(authService, times(1)).signIn(loginDTO);
    }

    @Test
    void testSignIn_InvalidLoginDTO() {
        LoginDTO loginDTO = createEmptyLoginDTO();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        var violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSignUp() throws RequestDataValidationFailedException, AuthenticationFailedException {
        UserDTO userDTO = createUserDTO();
        doNothing().when(authService).signUp(userDTO);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<SuccessDTO<String>> responseEntity = authController.signUp(userDTO, bindingResult);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Successfully signed up", responseEntity.getBody().getData());
        verify(authService, times(1)).signUp(userDTO);
    }

    @Test
    void testSignUp_InvalidUserDTO() {
        UserDTO userDTO = createEmptyUserDTO();

        var violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSignOut() throws AuthenticationFailedException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("access_token", "some_access_token");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        doNothing().when(redisHelper).add(eq("blacklist"), anyString());

        ResponseEntity<SuccessDTO<String>> responseEntity = authController.signOut(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Successfully signed out", responseEntity.getBody().getData());
        verify(request, times(1)).getCookies();
        verify(redisHelper, times(1)).add(eq("blacklist"), anyString());
    }

    @Test
    void testSignOut_InvalidAccessToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        assertThrows(AuthenticationFailedException.class, () -> authController.signOut(request));
        verify(request, times(1)).getCookies();
        verifyNoInteractions(jwtHelper, authService);
    }

    @Test
    void testGetCurrentUser() throws AuthenticationFailedException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("access_token", "some_access_token");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(jwtHelper.extractUserId("some_access_token")).thenReturn(1L);
        UserDTO userDTO = createUserDTO();
        when(authService.userInfo(1L)).thenReturn(userDTO);

        ResponseEntity<SuccessDTO<UserDTO>> responseEntity = authController.getCurrentUser(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Current User", responseEntity.getBody().getSubject());
        assertEquals(userDTO, responseEntity.getBody().getData());
        verify(request, times(1)).getCookies();
        verify(jwtHelper, times(1)).extractUserId("some_access_token");
        verify(authService, times(1)).userInfo(1L);
    }

    @Test
    void testGetCurrentUser_InvalidAccessToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        assertThrows(AuthenticationFailedException.class, () -> authController.getCurrentUser(request));
        verify(request, times(1)).getCookies();
        verifyNoInteractions(jwtHelper, authService);
    }

    private UserDTO createUserDTO() {
        return new UserDTO(
                1L,
                "John",
                "Doe",
                "johndoe",
                "pass",
                "johndoe@mail.example",
                "+79999999999",
                "01.01.2000",
                new ArrayList<>()
        );
    }

    private UserDTO createEmptyUserDTO() {
        return new UserDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private LoginDTO createLoginDTO() {
        return new LoginDTO(
                "johndoe@mail.example",
                "johndoe",
                "+79999999999",
                "pass"
        );
    }

    private LoginDTO createEmptyLoginDTO() {
        return new LoginDTO(
                null,
                null,
                null,
                null
        );
    }
}
