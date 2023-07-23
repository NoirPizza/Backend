package com.pizza.pizzashop.utils;

import com.pizza.pizzashop.exceptions.TokenValidationException;
import com.pizza.pizzashop.security.CustomUserDetails;
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

@Component
public class JWTHelper {
    @Value("${jwt.secret_key}")
    private String secretKeyString;

    @Value("${jwt.access_token.lifetime}")
    private Integer tokenLifetime;

    public String generateToken(Authentication authentication) {
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

    public Integer extractUserId(String token) {
        return Integer.valueOf(
                Jwts
                        .parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject());
    }

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
