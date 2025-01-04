package com.News.News.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JwtService {


    private final String secret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    SecretKey secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

    private final long expiration = 3600000;

    public String generateToken(String username, Long userId, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        //claims.put("username", username);
        claims.put("roles", authorities.getFirst());
        claims.put("id", userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.containsKey("id") ? Long.valueOf(claims.get("id").toString()) : null;
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object role = claims.get("roles"); // Extract the single role from the "roles" claim

        if (role instanceof String) {
            // Wrap the single role into a GrantedAuthority list
            return List.of(new SimpleGrantedAuthority((String) role));
        }
        return new ArrayList<>(); // Return an empty list if no role is found or invalid
    }

}
