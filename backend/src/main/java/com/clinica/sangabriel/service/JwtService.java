package com.clinica.sangabriel.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final Key secretKey;
    private static final long EXPIRATION_MS = 60L * 60 * 1000; // 1 hora

    public JwtService(@Value("${jwt.secret:}") String claveBase64) {
        if (claveBase64 == null || claveBase64.isBlank()) {
            throw new IllegalStateException("Define la variable JWT_SECRET antes de iniciar Spring Boot");
        }
        byte[] bytes = Decoders.BASE64.decode(claveBase64);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 bytes en Base64");
        }
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails usuario) {
        Claims claims = extractAllClaims(token);
        return usuario.isEnabled()
                && usuario.getUsername().equals(claims.getSubject())
                && claims.getExpiration() != null
                && claims.getExpiration().after(new Date());
    }

    public String generateToken(UserDetails usuario) {
        long ahora = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(ahora))
                .setExpiration(new Date(ahora + EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
