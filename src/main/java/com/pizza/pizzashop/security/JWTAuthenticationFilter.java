package com.pizza.pizzashop.security;

import com.pizza.pizzashop.security.CustomUserDetailsService.CustomUserDetailsService;
import com.pizza.pizzashop.utils.GlobalLogger;
import com.pizza.pizzashop.utils.JWTHelper;
import com.pizza.pizzashop.utils.RedisHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

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

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtHelper.validateToken(jwt) && !redisHelper.check("blacklist", jwt)) {
                Integer userId = jwtHelper.extractUserId(jwt);

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

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie cookieWithToken = WebUtils.getCookie(request, "access_token");
        return cookieWithToken != null ? cookieWithToken.getValue() : null;
    }
}
