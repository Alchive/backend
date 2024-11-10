package com.Alchive.backend.config.jwt;

import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 액세스 토큰 추출 및 검증
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            authenticateWithToken(accessToken);
        }
        // 액세스 토큰이 없거나 만료된 경우 리프레시 토큰 확인
        else {
            // 리프레시 토큰 추출 및 검증
            String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                String email = jwtTokenProvider.getEmailFromToken(refreshToken);
                // 새로운 액세스, 리프레시 토큰 발급
                String newAccessToken = jwtTokenProvider.createAccessToken(email);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(email);
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                response.setHeader("Refresh-Token", newRefreshToken);
                // 새로 발급된 액세스 토큰으로 인증 처리
                authenticateWithToken(newAccessToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateWithToken(String token) {
        // 토큰에서 이메일 추출 후 이메일로 사용자 조회
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userService.findByEmail(email);
        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null);
        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
