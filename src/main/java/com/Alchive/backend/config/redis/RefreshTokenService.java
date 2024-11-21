package com.Alchive.backend.config.redis;

import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void saveRefreshToken(String email, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(email, refreshToken));
    }

    public String getRefreshToken(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(NoSuchUserIdException::new);
        return refreshToken.getRefreshToken();
    }

    public String createRefreshToken(String email) {
        return jwtTokenProvider.createRefreshToken(email);
    }
}
