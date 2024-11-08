package com.Alchive.backend.service;

import com.Alchive.backend.dto.response.SnsResponseDTO;
import com.Alchive.backend.repository.SnsReporitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;

@RequiredArgsConstructor
@Service
public class SnsService {
    private final SnsReporitory snsReporitory;

    public SnsResponseDTO getSns(Long snsId) {
        return new SnsResponseDTO(snsReporitory.findById(snsId)
                .orElseThrow(NoSuchSnsIdException::new));
    }
}
