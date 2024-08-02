package com.nicolaspaiva.finance_vault.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

/**
 *
 */
public interface JwtService {

    // Extracts the username(email) from the token
    String extractUsername(String token);

    // Generates a JWT
    String generateToken(UserDetails userDetails);

    // Checks whether a JWT is valid
    boolean isTokenValid(String token, UserDetails userDetails);

    // Generates a refresh token
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
