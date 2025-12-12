package com.example.usermanagement.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthJwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(UserDetails ud) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, ud.getUsername(), accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails ud) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, ud.getUsername(), refreshTokenExpiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder().claims(claims).subject(subject).issuedAt(now).expiration(expiryDate).signWith(getSignKey()).compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails ud) {
        final String username = extractUserName(token);
        return (username.equals(ud.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isAccessToken(String token) {
        Date expiration = extractExpiration(token);
        Date issuedAt = extractClaim(token, Claims::getIssuedAt);
        long tokenLifeSpan = expiration.getTime() - issuedAt.getTime();
        return Math.abs(tokenLifeSpan - accessTokenExpiration) < 1000;
    }
}
