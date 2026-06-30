package com.core.security;

import com.core.constant.SecurityConstant;
import com.core.property.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtTokenHelper {

    private final JwtProperty jwtProperty;

    public String generateToken(String userName, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes()))
                .compact();
    }

    public String createResetToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtProperty.getExpiration()))
                .claim("purpose", SecurityConstant.PASSWORD_RESET)
                .signWith(Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes()))
                .compact();
    }

    public boolean isResetTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            String purpose = claims.get("purpose", String.class);
            return SecurityConstant.PASSWORD_RESET.equals(purpose) && !isTokenExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        Claims claims = parseClaims(token);
        return resolver.apply(claims);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims claims) {
        Date exp = claims.getExpiration();
        return exp == null || exp.before(new Date());
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        String id = (String) claims.get("userId");
        return UUID.fromString(id);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
