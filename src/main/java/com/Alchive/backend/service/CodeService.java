package com.Alchive.backend.service;

import com.Alchive.backend.domain.Code;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.dto.request.CodeUpdateRequest;
import com.Alchive.backend.repository.CodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CodeService {
    private final CodeRepository codeRepository;

    @Transactional
    public void updateSolution(CodeUpdateRequest request) {
        Long codeId = request.getCodeId();
        String solution = request.getSolution();
        Code code = codeRepository.findByCodeId(codeId)
                .orElseThrow(() -> new NoSuchIdException(com.Alchive.backend.config.Code.CODE_NOT_FOUND, codeId));
        code.update(solution);
    }
}
