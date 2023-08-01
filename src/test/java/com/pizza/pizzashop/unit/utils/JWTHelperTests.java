package com.pizza.pizzashop.unit.utils;

import com.pizza.pizzashop.exceptions.TokenValidationException;
import com.pizza.pizzashop.security.CustomUserDetails;
import com.pizza.pizzashop.utils.JWTHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JWTHelperTests {
    @Mock
    private Authentication authentication;
    @Mock
    private CustomUserDetails userDetails;
    @InjectMocks
    private JWTHelper jwtHelper;
    private final String secretKeyString = "dmVyeV9yZWFsbHlfc2VjcmV0X3Bhc3NfdWx0cmFfMV9hZGFGQVNER0ZHREZHZHNmZ2RmZw==";
    private final Integer tokenLifetime = 6480000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtHelper.setSecretKeyString(secretKeyString);
        jwtHelper.setTokenLifetime(tokenLifetime);
    }

    @Test
    void testGenerateToken() throws Exception {
        Long userId = 123L;
        when(userDetails.getId()).thenReturn(userId);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token =  jwtHelper.generateToken(authentication);
        assertNotNull(token);
        assertTrue(jwtHelper.validateToken(token));

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                .build()
                .parseClaimsJws(token);

        assertEquals("Pizza Noir", claims.getBody().getIssuer());
        assertEquals(userId.toString(), claims.getBody().getSubject());
    }

    @Test
    void testValidateToken_() throws TokenValidationException {
        String token = generateValidToken();

        assertTrue(jwtHelper.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "totally.invalid.token";

        assertThrows(TokenValidationException.class ,() -> jwtHelper.validateToken(invalidToken));
    }

    @Test
    void testExtractUserId() {
        Long userId = 123L;
        String token = generateValidToken(userId);

        Long extractedUserId = jwtHelper.extractUserId(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    void testExtractUserId_InvalidToken() {
        String invalidToken = "invalid_token";

        assertThrows(Exception.class, () -> jwtHelper.extractUserId(invalidToken));
    }

    private String generateValidToken() {
        return generateValidToken(123L);
    }

    private String generateValidToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Pizza Noir")
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifetime))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString)))
                .compact();
    }
}
