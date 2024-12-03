package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.dto.response.SnsResponseDTO;
import com.Alchive.backend.repository.SnsReporitory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SnsService {
    private final SnsReporitory snsReporitory;

    public SnsResponseDTO getSns(Long snsId) {
        return new SnsResponseDTO(snsReporitory.findById(snsId)
                .orElseThrow(NoSuchSnsIdException::new));
    }

    @Transactional
    public void createSns(User user, SnsCreateRequest request) {
        Sns sns = Sns.of(user, request);
        snsReporitory.save(sns);
    }
}
