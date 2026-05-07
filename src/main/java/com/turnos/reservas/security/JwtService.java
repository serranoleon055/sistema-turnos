package com.turnos.reservas.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Esto literalmente te lo copie y pegue
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {
        // Esto literalmente te lo copie y pegue
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims sujeto = extractAllClaims(token);
        String email = sujeto.getSubject();
        return email;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims sujeto = extractAllClaims(token);
        return !sujeto.getExpiration().before(new Date()) && userDetails.getUsername().equals(sujeto.getSubject());
    }

    public Date extractExpiration(String token) {
        Claims sujeto = extractAllClaims(token);
        return sujeto.getExpiration();
    }

    public Claims extractAllClaims(String token) {
        // Esto literalmente te lo copie y pegue
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSigningKey() {
        // Esto literalmente te lo copie y pegue
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
