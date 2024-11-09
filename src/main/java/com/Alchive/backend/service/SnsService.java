package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.dto.response.SnsResponseDTO;
import com.Alchive.backend.repository.SnsReporitory;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;

@RequiredArgsConstructor
@Service
public class SnsService {
    private final TokenService tokenService;
    private final SnsReporitory snsReporitory;
    private final UserRepository userRepository;

    public SnsResponseDTO getSns(Long snsId) {
        return new SnsResponseDTO(snsReporitory.findById(snsId)
                .orElseThrow(NoSuchSnsIdException::new));
    }

    @Transactional
    public void createSns(HttpServletRequest tokenRequest, SnsCreateRequest request) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
        Sns sns = Sns.of(user, request);
        snsReporitory.save(sns);
    }
}
