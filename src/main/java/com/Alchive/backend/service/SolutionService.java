package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.domain.Solution;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;

    @Transactional
    public void updateSolution(SolutionUpdateRequest request) {
        Long solutionId = request.getSolutionId();
        String content = request.getContent();
        Solution code = solutionRepository.findBySolutionId(solutionId)
                .orElseThrow(() -> new NoSuchIdException(Code.SOLUTION_NOT_FOUND, solutionId));
        code.update(content);
    }
}
