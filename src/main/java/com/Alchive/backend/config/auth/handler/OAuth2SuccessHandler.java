package com.Alchive.backend.config.auth.handler;

import com.Alchive.backend.config.jwt.TokenService;
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
    private final TokenService tokenService;
    private final UserRepository userRepository;

    // 검증 완료된 유저의 정보를 가져와서 토큰 생성, 로그인/회원가입 요청에 맞게 리다이렉트
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userRepository.findByUserEmail(email);
        String targetUrl;
        Long userId;

        if ( user.isPresent() ) { // 로그인인 경우
            userId = user.get().getUserId();
            String accessToken = tokenService.generateAccessToken(userId);
            String refreshToken = tokenService.generateRefreshToken();
            targetUrl = UriComponentsBuilder.fromUriString("/")
                    .queryParam("access", accessToken)
                    .queryParam("refresh", refreshToken)
                    .build().toUriString();
        }
        else { // 회원가입인 경우
            targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/signin")
                    .queryParam("email",email)
                    .build().toUriString();
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
