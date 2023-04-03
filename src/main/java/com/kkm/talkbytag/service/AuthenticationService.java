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
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String extractUsername(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public String generateToken(String username) {

        // Set expiration date
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // Generate token
        return Jwts.builder()
                .signWith(KEY, SignatureAlgorithm.HS256)
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

