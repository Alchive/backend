package com.Alchive.backend.config.jwt;

import com.Alchive.backend.config.error.exception.token.TokenExpiredException;
import com.Alchive.backend.config.error.exception.token.TokenNotExistsException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    // JWT 토큰 생성
    private Key secretKey;
    @Value("${jwt.token.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.token.access-expire-length}")
    private Long ACCESS_EXPIRE_LENGTH;
    @Value("${jwt.token.refresh-expire-length}")
    private Long REFRESH_EXPIRE_LENGTH;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    // 액세스 및 리프레시 토큰 생성
    public String createAccessToken(String email) {
        return createToken(email, ACCESS_EXPIRE_LENGTH);
    }

    public String createRefreshToken(String email) {
        return createToken(email, REFRESH_EXPIRE_LENGTH);
    }

    private String createToken(String email, Long expireLength) {
        Claims claims = Jwts.claims().setSubject(email);
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireLength))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 액세스 토큰 추출
    public String resolveAccessToken(HttpServletRequest request) {
        return resolveToken(request, "Authorization", "Bearer ");
    }

    // 리프레시 토큰 추출
    public String resolveRefreshToken(HttpServletRequest request) {
        return resolveToken(request, "Refresh-Token", "");
    }

    // HTTP 요청 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request, String headerName, String prefix) {
        try {
            String header = request.getHeader(headerName);
            return prefix.isEmpty() ? header : header.substring(prefix.length());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new TokenNotExistsException();
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException e) {
            throw e;
        }
    }

    // 이메일 추출
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException exception) {
            return exception.getClaims().getSubject();
        }
    }
}
