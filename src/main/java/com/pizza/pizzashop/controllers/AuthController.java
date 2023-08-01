package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.basic.SuccessDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.exceptions.RequestDataValidationFailedException;
import com.pizza.pizzashop.services.AuthService.AuthService;
import com.pizza.pizzashop.utils.GlobalExceptionHandler;
import com.pizza.pizzashop.utils.JWTHelper;
import com.pizza.pizzashop.utils.RedisHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

/**
 * This class handles authentication-related API endpoints for user sign-in, sign-up, and sign-out operations.
 * It provides methods to perform user authentication, generate and manage JWT (JSON Web Token) for users,
 * and interact with Redis for token management.
 */
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Dependencies required for authentication and user-related services
    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwtHelper;
    private final RedisHelper redisHelper;
    private final AuthService authService;

    private static final String COOKIE_TOKEN_NAME = "access_token";

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JWTHelper jwtHelper,
            RedisHelper redisHelper,
            AuthService authService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.redisHelper = redisHelper;
        this.authService = authService;
    }

    /**
     * Handle HTTP POST requests to "/api/auth/sign-in" for user sign-in.
     *
     * @param loginDTO         The login DTO containing user login credentials.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @param response         The HttpServletResponse object used to add an HTTP-only cookie for the access token.
     * @return A ResponseEntity containing a SuccessDTO with the UserDTO representing the signed-in user.
     * @throws RequestDataValidationFailedException     If there are validation errors in the loginDTO.
     * @throws AuthenticationFailedException If user authentication fails during the sign-in process.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<SuccessDTO<UserDTO>> singIn(
            @Valid @RequestBody LoginDTO loginDTO,
            BindingResult validationResult,
            HttpServletResponse response
    ) throws RequestDataValidationFailedException, AuthenticationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO userDTO = authService.signIn(loginDTO);

        Cookie accessTokenCookie = new Cookie(COOKIE_TOKEN_NAME, jwtHelper.generateToken(authentication));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Sign In",
                        userDTO
                ), HttpStatus.OK);
    }

    /**
     * Handle HTTP POST requests to "/api/auth/sign-up" for user sign-up.
     *
     * @param userDTO          The user DTO containing user details for sign-up.
     * @param validationResult The BindingResult object that holds validation errors, if any.
     * @return A ResponseEntity containing a SuccessDTO with a message indicating successful sign-up.
     * @throws RequestDataValidationFailedException     If there are validation errors in the loginDTO.
     * @throws AuthenticationFailedException If user authentication fails during the sign-in process.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<SuccessDTO<String>> signUp(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult validationResult
    ) throws RequestDataValidationFailedException, AuthenticationFailedException {
        if (validationResult.hasErrors()) {
            throw new RequestDataValidationFailedException(GlobalExceptionHandler.handleValidationResults(validationResult));
        }
        authService.signUp(userDTO);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.CREATED.value(),
                        "Sign Up",
                        "Successfully signed up"
                ), HttpStatus.CREATED);
    }

    /**
     * Handle HTTP POST requests to "/api/auth/sign-out" for user sign-out.
     *
     * @param request The HttpServletRequest object used to extract the access token from the cookie.
     * @return A ResponseEntity containing a SuccessDTO with a message indicating successful sign-out.
     * @throws AuthenticationFailedException If user authentication fails while retrieving user information.
     */
    @PostMapping("/sign-out")
    public ResponseEntity<SuccessDTO<String>> signOut(HttpServletRequest request) throws AuthenticationFailedException {
        Cookie tokenCookie = WebUtils.getCookie(request, COOKIE_TOKEN_NAME);
        if (tokenCookie == null) {
            throw new AuthenticationFailedException("No access token was found");
        }
        String accessToken = tokenCookie.getValue();
        redisHelper.add("blacklist", accessToken);
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Sign Out",
                        "Successfully signed out"
                ), HttpStatus.OK);
    }

    /**
     * Handle HTTP GET requests to "/api/auth/current-user" for fetching the current logged-in user's information.
     *
     * @param request The HttpServletRequest object used to extract the access token from the cookie.
     * @return A ResponseEntity containing a SuccessDTO with the UserDTO representing the current user.
     * @throws AuthenticationFailedException If user authentication fails while retrieving user information.
     */
    @GetMapping("/current-user")
    public ResponseEntity<SuccessDTO<UserDTO>> getCurrentUser(
            HttpServletRequest request
    ) throws AuthenticationFailedException {
        Cookie tokenCookie = WebUtils.getCookie(request, COOKIE_TOKEN_NAME);
        if (tokenCookie == null) {
            throw new AuthenticationFailedException("No access token was found");
        }
        String accessToken = tokenCookie.getValue();
        UserDTO userDTO = authService.userInfo(jwtHelper.extractUserId(accessToken));
        return new ResponseEntity<>(
                new SuccessDTO<>(
                        HttpStatus.OK.value(),
                        "Current User",
                        userDTO
                ), HttpStatus.OK);
    }
}
