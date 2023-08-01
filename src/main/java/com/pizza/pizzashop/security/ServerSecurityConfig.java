package com.pizza.pizzashop.security;

import com.pizza.pizzashop.security.CustomUserDetailsService.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is a configuration class responsible for configuring Spring Security for the server application.
 * It enables Web Security and Method Security and defines security-related configurations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ServerSecurityConfig {
    protected static final String[] ENDPOINTS_WHITELIST = {
            "/api/auth/sign-in",
            "/api/auth/sign-up"
    };
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public ServerSecurityConfig(
            CustomUserDetailsServiceImpl customUserDetailsService,
            JWTAuthenticationFilter jwtAuthenticationFilter,
            JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    /**
     * Configures the security filter chain for the HTTP requests.
     *
     * @param http The HttpSecurity object to configure security on incoming HTTP requests.
     * @return The SecurityFilterChain object representing the configured security filter chain.
     * @throws Exception Exception If there is an error during configuration.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                                .anyRequest().authenticated());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configures the AuthenticationManager bean.
     *
     * @return The configured AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    /**
     * Configures the AuthenticationManager bean using the AuthenticationConfiguration.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration instance to get the AuthenticationManager.
     * @return The configured AuthenticationManager.
     * @throws Exception Exception If there is an error during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Creates a PasswordEncoder bean for encoding passwords.
     *
     * @return The PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
