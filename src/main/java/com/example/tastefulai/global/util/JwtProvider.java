package com.example.tastefulai.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiryMillis;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiryMillis;

    public String generateToken(String email, long expiryMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            log.error("JWT 검증 실패: {}", exception.getMessage());
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpiryMillis);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpiryMillis);
    }
}