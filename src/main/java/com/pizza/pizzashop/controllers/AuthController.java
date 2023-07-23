package com.pizza.pizzashop.controllers;

import com.pizza.pizzashop.dtos.LoginDTO;
import com.pizza.pizzashop.dtos.SuccessDTO;
import com.pizza.pizzashop.dtos.UserDTO;
import com.pizza.pizzashop.exceptions.AuthenticationFailedException;
import com.pizza.pizzashop.exceptions.ValidationFailedException;
import com.pizza.pizzashop.services.AuthService.AuthService;
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

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTHelper jwtHelper;
    private final RedisHelper redisHelper;
    private final AuthService authService;

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


    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> singIn(
            @Valid @RequestBody LoginDTO loginDTO,
            BindingResult result,
            HttpServletResponse response
    ) throws ValidationFailedException, AuthenticationFailedException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDTO user = authService.signIn(loginDTO);

        Cookie accessTokenCookie = new Cookie("access_token", jwtHelper.generateToken(authentication));
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SuccessDTO> signUp(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) throws ValidationFailedException, AuthenticationFailedException {
        if (result.hasErrors()) {
            throw new ValidationFailedException("Validation failed.\n" + result.getAllErrors());
        }
        authService.signUp(userDTO);
        return new ResponseEntity<>(
                new SuccessDTO(
                        HttpStatus.OK.value(),
                        "Sign Up",
                        "Successfully signed up"
                ), HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    public void signOut(HttpServletRequest request) {
        String accessToken = Objects.requireNonNull(WebUtils.getCookie(request, "access_token")).getValue();
        redisHelper.add("blacklist", accessToken);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) throws AuthenticationFailedException {
        String accessToken = Objects.requireNonNull(WebUtils.getCookie(request, "access_token")).getValue();
        UserDTO userDTO = authService.userInfo(jwtHelper.extractUserId(accessToken));
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
