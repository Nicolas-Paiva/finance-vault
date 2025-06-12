package com.finance_vault.finance_vault.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final String secretKey = "de6c5b5b556b5f82688e3f0329b75114f3e6eaaeb460b2bcc59ea3ff5f67727865d510c5057dc8e0590158383e8d4c40f4f885ac4239807e861173cd958807eb69494526ca8d32657205b2f90ecdc567db867933998ce4ffb0ef741126031053ac29a62caad78226273eda8711e1c49baa8698918f81f2df3752e65c17d850e7acb9b228b8d52c474a896a163eb7ed72cc7e1adeadf9ab191072b6fac0bc12dae10162db8c7aa1b6fc70f3579287794d41e785395f2de7bdc4bc742d83c24ea8091f465cb3a2be595081eeaa2db64efc98d2da3b55f4f544f54a1bd1e5f3a41d9b97b990b5cfa4cad2caaf7c2430524ba07a8ba295e7e2e01574be38ddea66c6";


    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        // 30min expiration
        int expiration = 1000 * 60 * 30;
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getKey())
                .compact();
    }


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // Extracts the email from the JWT
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
