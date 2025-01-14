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

    // JWT secret-key
    @Value("${jwt.secret}")
    private String secret;

    // access-token 만료 시간
    @Getter
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiryMillis;

    // refresh-token 만료 시간
    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiryMillis;

    // JWT token 생성
    public String generateToken(String email, long expiryMillis) {
        // 현재 시간
        Date now = new Date();
        // 만료 시간
        Date expiryDate = new Date(now.getTime() + expiryMillis);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 유효성 검증
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

    // JWT 토큰을 통한 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Access-Token 생성
    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpiryMillis);
    }

    // Refresh-Token 생성
    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpiryMillis);
    }
}