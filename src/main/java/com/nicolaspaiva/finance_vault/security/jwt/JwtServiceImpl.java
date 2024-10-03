package com.nicolaspaiva.finance_vault.security.jwt;

import com.nicolaspaiva.finance_vault.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService{


    /**
     * Generates a JWT based on the UserDetails object
     */
    @Override
    public String generateToken(UserDetails userDetails){

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .issuer("Finance Vault")
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }


    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .claim("roles", Role.USER)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 72))
                .issuer("Finance Vault")
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }


    /**
     * Generates a Key used to sign a JWT
     */
    private SecretKey getSigningKey(){
        byte[] key = Decoders.BASE64
                .decode("a2ca511cf2c7ec5b98f8a629b2140c30c1dd9405253d1ccbebdbe39e944a620c");
        return Keys.hmacShaKeyFor(key);
    }


    /**
     * Extracts a specific claim from a JWT
     * @param claimsResolver a function used to extract a specific claim
     * @return the specified claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Extracts all the claims from the JWT
     */
    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
    }


    /**
     * Extracts the username (email) from the JWT
     */
    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    /**
     * Verifies whether the JWT token is valid,
     * checking the username and the expiration
     * date in the token
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


}
