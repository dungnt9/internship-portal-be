package com.dungnguyen.auth_service.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKeyWithAtLeast256BitsLengthForHS256Algorithm}")
    private String secret;

    @Value("${jwt.expiration:604800000}") // 7 days in milliseconds
    private long jwtExpiration;

    public String generateToken(Integer userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userId.toString());
    }

    /**
     * @throws ExpiredJwtException if token is expired
     * @throws SignatureException if token signature is invalid
     * @throws MalformedJwtException if token is malformed
     */
    public Boolean validateToken(String token, String userId) {
        final String extractedUserId = extractUserId(token);

        if (!extractedUserId.equals(userId)) {
            log.warn("Token contains user ID {} but expected {}", extractedUserId, userId);
            return false;
        }

        if (isTokenExpired(token)) {
            log.warn("Token is expired");
            return false;
        }

        return true;
    }

    /**
     * @return User ID
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract creation date from token
     * @param token JWT token
     * @return Creation date
     */
    public Date extractCreationDate(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Extract claim from token
     * @param token JWT token
     * @param claimsResolver Function to resolve claim
     * @return Claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * @return Claims
     * @throws ExpiredJwtException if token is expired
     * @throws SignatureException if token signature is invalid
     * @throws MalformedJwtException if token is malformed
     */
    private Claims extractAllClaims(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.error("JWT token error: {}", e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * @param claims Claims to include in token
     * @param subject Subject (user ID)
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}