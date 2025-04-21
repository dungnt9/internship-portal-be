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

    // Secret key should be at least 256 bits for HS256 algorithm
    @Value("${jwt.secret:defaultSecretKeyWithAtLeast256BitsLengthForHS256Algorithm}")
    private String secret;

    @Value("${jwt.expiration:604800000}") // 7 days in milliseconds
    private long jwtExpiration;

    /**
     * Generate token for user
     * @param username Username
     * @param email User email
     * @param phone User phone number
     * @param role User role
     * @return JWT token
     */
    public String generateToken(String username, String email, String phone, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        if (email != null) claims.put("email", email);
        if (phone != null) claims.put("phone", phone);
        return createToken(claims, username);
    }

    /**
     * Validate token
     * @param token JWT token
     * @param username Username to validate
     * @return true if token is valid
     * @throws ExpiredJwtException if token is expired
     * @throws SignatureException if token signature is invalid
     * @throws MalformedJwtException if token is malformed
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);

        if (!extractedUsername.equals(username)) {
            log.warn("Token contains username {} but expected {}", extractedUsername, username);
            return false;
        }

        if (isTokenExpired(token)) {
            log.warn("Token is expired");
            return false;
        }

        return true;
    }

    /**
     * Extract username from token
     * @param token JWT token
     * @return Username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract email from token
     * @param token JWT token
     * @return Email or null if not present
     */
    public String extractEmail(String token) {
        return (String) extractAllClaims(token).get("email");
    }

    /**
     * Extract phone from token
     * @param token JWT token
     * @return Phone number or null if not present
     */
    public String extractPhone(String token) {
        return (String) extractAllClaims(token).get("phone");
    }

    /**
     * Extract role from token
     * @param token JWT token
     * @return User role
     */
    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    /**
     * Extract expiration date from token
     * @param token JWT token
     * @return Expiration date
     */
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
     * Check if token is about to expire (within 1 day)
     * @param token JWT token
     * @return true if token is about to expire
     */
    public Boolean isTokenAboutToExpire(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis() < 86400000; // 24 hours
    }

    /**
     * Extract all claims from token
     * @param token JWT token
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

    /**
     * Check if token is expired
     * @param token JWT token
     * @return true if token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Create token
     * @param claims Claims to include in token
     * @param subject Subject (username)
     * @return JWT token
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