package com.pizza.pizzashop.security;

import com.pizza.pizzashop.security.CustomUserDetailsService.CustomUserDetailsService;
import com.pizza.pizzashop.utils.GlobalLogger.GlobalLogger;
import com.pizza.pizzashop.utils.JWTHelper;
import com.pizza.pizzashop.utils.RedisHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

/**
 * This class is a component that extends the OncePerRequestFilter class.
 * It is responsible for filtering incoming HTTP requests and authenticating users
 * based on the JWT token provided in the "access_token" cookie.
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTHelper jwtHelper;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisHelper redisHelper;

    @Autowired
    public JWTAuthenticationFilter(
            JWTHelper jwtHelper,
            CustomUserDetailsService customUserDetailsService,
            RedisHelper redisHelper
    ) {
        this.jwtHelper = jwtHelper;
        this.customUserDetailsService = customUserDetailsService;
        this.redisHelper = redisHelper;
    }

    /**
     * This method is called for each incoming HTTP request and is responsible for handling JWT-based authentication.
     * It validates the JWT token, checks if it is not blacklisted in Redis, and sets the authentication in the security context.
     * If the token is not valid or blacklisted, the request continues without authentication.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to process the request and response.
     * @throws ServletException If there is a servlet-related problem during processing.
     * @throws IOException      If there is an I/O related problem during processing.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtHelper.validateToken(jwt) && !redisHelper.check("blacklist", jwt)) {
                Long userId = jwtHelper.extractUserId(jwt);

                CustomUserDetails userDetails = customUserDetailsService.loadById(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception ex) {
            GlobalLogger.log("ERROR", String.valueOf(ex));
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to extract the JWT token from the "access_token" cookie in the HTTP request.
     *
     * @param request The HTTP request.
     * @return The JWT token as a String, or null if the token is not found.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie cookieWithToken = WebUtils.getCookie(request, "access_token");
        return cookieWithToken != null ? cookieWithToken.getValue() : null;
    }
}
