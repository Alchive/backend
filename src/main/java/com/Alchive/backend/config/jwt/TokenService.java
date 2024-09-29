package com.Alchive.backend.config.jwt;

import com.Alchive.backend.config.error.exception.token.TokenExpiredException;
import com.Alchive.backend.config.error.exception.token.TokenNotExistsException;
import com.Alchive.backend.config.error.exception.token.UnmatchedUserIdException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class TokenService {
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

    private String generateToken(Long expireLength, Claims claims) {
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireLength))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        return generateToken(ACCESS_EXPIRE_LENGTH, claims);
    }

    public String generateRefreshToken() {
        return generateToken(REFRESH_EXPIRE_LENGTH, Jwts.claims());
    }

    private String resolveToken(HttpServletRequest request, String headerName, String prefix) {
        try {
            String header = request.getHeader(headerName);
            return prefix.isEmpty() ? header : header.substring(prefix.length());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new TokenNotExistsException();
        }
    }

    // JWT 검증 로직에서 userId 반환
    private Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException exception) {
            throw new TokenExpiredException();
        } catch (IllegalArgumentException e) {
            throw new TokenNotExistsException();
        }
    }

    // 액세스 토큰 검증 후 userId 반환
    public Long validateAccessToken(HttpServletRequest request) {
        String token = resolveToken(request, "AUTHORIZATION", "Bearer ");
        Claims claims = validateToken(token); // JWT 검증 및 claims 반환
        return Long.parseLong(claims.getSubject()); // userId 반환
    }

    public void validateRefreshToken(HttpServletRequest request) {
        String token = resolveToken(request, "REFRESH-TOKEN", "");
        validateToken(token); // 검증만 진행
    }

    private Claims getClaimsWithoutExpirationCheck(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // 만료된 토큰이라도 Claims를 반환
            return e.getClaims();
        }
    }

    // 리프레시 토큰으로 새로운 액세스 토큰 발급
    public String refreshAccessToken(HttpServletRequest request) {
        validateRefreshToken(request); // 리프레시 토큰 검증
        String token = resolveToken(request, "AUTHORIZATION", "Bearer ");
        Claims claims = getClaimsWithoutExpirationCheck(token);
        return generateAccessToken(Long.parseLong(claims.getSubject()));
    }
}
