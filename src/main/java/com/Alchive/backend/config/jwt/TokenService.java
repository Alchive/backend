package com.Alchive.backend.config.jwt;

import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.token.TokenExpiredException;
import com.Alchive.backend.config.error.exception.token.TokenNotExistsException;
import com.Alchive.backend.repository.UserRepository;
import io.jsonwebtoken.*;
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

    private final UserRepository userRepository;

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateAccessToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_LENGTH))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(HttpServletRequest request) {
        try {
            String token = resolveAccessToken(request);
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token);

            return claims.getBody().getExpiration()
                    .after(new Date(System.currentTimeMillis()));

        } catch (Exception e) {
            throw new TokenExpiredException("access token");
        }
    }

    public boolean validateRefreshToken(HttpServletRequest request) {
        try {
            String token = resolveRefreshToken(request);
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token);

            return claims.getBody().getExpiration()
                    .after(new Date(System.currentTimeMillis()));

        } catch (Exception e) {
            throw new TokenExpiredException("refresh token");
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String header = request.getHeader("AUTHORIZATION");
        try {
            String token = header.substring("Bearer ".length());
            return token;
        } catch (Exception e ){
            throw new TokenNotExistsException("access token");
        }
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("REFRESH-TOKEN");
            return token;
        } catch (Exception e) {
            throw new TokenNotExistsException("refresh token");
        }
    }

    // 리프레시 토큰으로 새로운 액세스 토큰 발급
    public String refreshAccessToken(HttpServletRequest request) {
        String refreshToken = resolveRefreshToken(request);
        if (refreshToken == null ) { // 리프레시 토큰이 없는 경우
            throw new TokenNotExistsException("refresh token");
        }
        validateRefreshToken(request); // 리프레시 토큰 검증
        Long userId = getUserIdFromToken(request);
        String accessToken = generateAccessToken(userId);
        return accessToken;
    }

    public Long getUserIdFromToken(HttpServletRequest request) { // 토큰에서 userId 정보 꺼내기
        String token = resolveAccessToken(request);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = Long.parseLong(claims.getSubject());
            userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchUserIdException(userId)); // user 검증
            return userId;
        } catch ( ExpiredJwtException exception ) {
            Claims expiredClaims = exception.getClaims();
            Long userId = Long.parseLong(expiredClaims.getSubject());
            userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchUserIdException(userId)); // user 검증
            return userId;
        }
    }
}
