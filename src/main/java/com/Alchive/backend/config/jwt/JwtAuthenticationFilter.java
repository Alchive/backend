package com.Alchive.backend.config.jwt;

import com.Alchive.backend.config.error.exception.token.TokenNotExistsException;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    // 인증에서 제외할 URL과 메서드
    private static final Map<String, List<String>> EXCLUDE_URL = Map.ofEntries(
            Map.entry("/api/v2", List.of("GET")),
            Map.entry("/api/v2/api-docs/**", List.of("GET")),
            Map.entry("/api/swagger-ui/**", List.of("GET")),
            Map.entry("/api/v2/boards", List.of("GET")),
            Map.entry("/api/v2/boards/{boardId}", List.of("GET")),
            Map.entry("/api/v2/users", List.of("GET", "POST")),
            Map.entry("/api/v2/users/{userId}", List.of("GET")),
            Map.entry("/api/v2/users/username/{name}", List.of("GET")),
            Map.entry("/api/v2/sns/{snsId}", List.of("GET")),
            Map.entry("/api/v2/slack/reminder", List.of("GET")),
            Map.entry("/api/v2/slack/added", List.of("GET"))
    );

    // EXCLUDE_URL과 메서드에 일치할 경우 현재 필터를 진행하지 않고 다음 필터 진행
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();
        return EXCLUDE_URL.entrySet().stream()
                .anyMatch(entry -> pathMatches(entry.getKey(), path) && entry.getValue().contains(method));
    }

    // 경로 패턴을 비교하는 유틸리티 메서드 (단순히 String 비교를 넘어 패턴 매칭이 필요할 경우 활용)
    private boolean pathMatches(String pattern, String path) {
        return pathMatcher.match(pattern, path);
    }

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
            } else {
                // 토큰이 없는 경우나 유효하지 않은 경우 에러 응답을 반환
                throw new TokenNotExistsException();
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
