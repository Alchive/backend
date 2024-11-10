package com.Alchive.backend.config.auth.handler;

import com.Alchive.backend.config.jwt.JwtTokenProvider;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 검증 완료된 유저의 정보를 가져와서 토큰 생성, 로그인/회원가입 요청에 맞게 리다이렉트
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userRepository.findByEmail(email);
        String targetUrl;
        String userEmail;

        if (user.isPresent()) { // 로그인인 경우
            userEmail = user.get().getEmail();
            String accessToken = jwtTokenProvider.createAccessToken(userEmail);
            String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);
            targetUrl = UriComponentsBuilder.fromUriString("/")
                    .queryParam("access", accessToken)
                    .queryParam("refresh", refreshToken)
                    .build().toUriString();
        } else { // 회원가입인 경우
            targetUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/sign")
                    .queryParam("email", email)
                    .build().toUriString();
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
