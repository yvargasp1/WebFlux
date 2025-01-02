package com.webflux.webflux.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Set "sub" claim
                .claim("roles", userDetails.getAuthorities()) // Set "roles" claim
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(getPublicKey(secret)) // Sign the token
                .compact();
    }
    public Claims getClaims(String token) {
        return Jwts.parser() // Use parserBuilder instead of parser
                .verifyWith(getPublicKey(secret)) // Set the signing key
                .build() // Build the parser
                .parseSignedClaims(token).getPayload(); // Parse the token
    }
    public String getSubject(String token) {
        return Jwts.parser() // Use parserBuilder instead of parser
                .verifyWith(getPublicKey(secret)) // Set the signing key
                .build() // Build the parser
                .parseSignedClaims(token).getPayload().getSubject(); // Parse the token
    }
    public boolean validate(String token){
        try {
            Jwts.parser() // Use parserBuilder instead of parser
                    .verifyWith(getPublicKey(secret)) // Set the signing key
                    .build() // Build the parser
                    .parseSignedClaims(token).getPayload();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private SecretKey getPublicKey(String secret) {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
