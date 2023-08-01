package com.pizza.pizzashop.utils;

import com.pizza.pizzashop.exceptions.TokenValidationException;
import com.pizza.pizzashop.security.CustomUserDetails;
import com.pizza.pizzashop.utils.GlobalLogger.GlobalLogger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides utility methods for handling JSON Web Tokens (JWT) used for user authentication.
 * It is responsible for generating, validating, and extracting information from JWTs.
 */
@Component
public class JWTHelper {
    @Value("${jwt.secret_key}")
    private String secretKeyString;

    @Value("${jwt.access_token.lifetime}")
    private Integer tokenLifetime;

    public void setSecretKeyString(String secretKeyString) {
        this.secretKeyString = secretKeyString;
    }

    public void setTokenLifetime(Integer tokenLifetime) {
        this.tokenLifetime = tokenLifetime;
    }

    /**
     * Generates a JWT based on the provided authentication object.
     *
     * @param authentication The authentication object containing user details.
     * @return The generated JWT as a string.
     */
    public String generateToken(Authentication authentication) {
        GlobalLogger.log("ERROR", secretKeyString);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Pizza Noir")
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifetime))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT to ensure it is well-formed and not expired.
     *
     * @param token The JWT to validate.
     * @return True if the JWT is valid and not expired; otherwise, false.
     * @throws TokenValidationException TokenValidationException If the token is not valid or has expired.
     */
    public boolean validateToken(String token) throws TokenValidationException {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            throw new TokenValidationException(e.getMessage());
        }
    }

    /**
     * Extracts the user ID from a JWT.
     *
     * @param token The JWT from which to extract the user ID.
     * @return The user ID as an integer.
     */
    public Long extractUserId(String token) {
        return Long.valueOf(
                Jwts
                        .parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject());
    }

    /**
     * Helper method to check if a JWT has expired.
     *
     * @param token The JWT to check.
     * @return True if the JWT has expired; otherwise, false.
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}
