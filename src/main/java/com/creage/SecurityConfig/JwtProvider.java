package com.creage.SecurityConfig;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

    private static final SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 86400000L; // 24 hours

    public static String generateToken(Authentication auth) {
        String email = auth.getName();
        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Extract writeaccess safely from authentication details
        int writeaccess = 0;
        Object details = auth.getDetails();
        if (details instanceof Integer) {
            writeaccess = (Integer) details;
        } else if (details instanceof Map) {
            // Optional enhancement: if you decide to use a Map for more detail in future
            Object rid = ((Map<?, ?>) details).get("writeaccess");
            if (rid instanceof Integer) {
                writeaccess = (Integer) rid;
            }
        }

        return Jwts.builder()
                .setSubject(email)
                .claim("email", email)
                .claim("authorities", authorities)
                .claim("writeaccess", writeaccess)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L)) // 24 hours
                .signWith(key)
                .compact();
    }


    public static String getEmailFromJwtToken(String jwt) {
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return String.valueOf(claims.get("email"));
    }
    
    
    public static String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token valid for 1 hour
                .signWith(key)
                .compact();
    }
    
    
    public static String validatePasswordResetToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return null; // Token is expired
            }

            return claims.getBody().getSubject(); // Return email from token
        } catch (JwtException e) {
            return null; // Invalid token
        }
    }


}
