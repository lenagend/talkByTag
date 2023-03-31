package com.kkm.talkbytag.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private static final String SECRET = "3jKJFq3Zq9c6Y5U6j5YZL2F2R6EgH2vF";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String extractUsername(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public String generateToken(String username) {

        // Set expiration date
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // Generate token
        return Jwts.builder()
                .signWith(KEY)
                .setSubject(username)
                .setIssuer("talkByTag")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

