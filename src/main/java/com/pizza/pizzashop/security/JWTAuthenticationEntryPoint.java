package com.pizza.pizzashop.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The JWTAuthenticationEntryPoint class is a component that implements the AuthenticationEntryPoint interface.
 * It handles unauthorized requests by sending an HTTP 401 Unauthorized response with an error message.
 */
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * This method is called when a user tries to access a protected resource without proper authentication.
     * It sends an HTTP 401 Unauthorized response with an error message.
     *
     * @param request       The HTTP request.
     * @param response      The HTTP response.
     * @param authException The authentication exception that caused the unauthorized access.
     * @throws IOException If an I/O exception occurs while sending the response.
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sorry, You're not authorized to access this resource.");
    }
}
