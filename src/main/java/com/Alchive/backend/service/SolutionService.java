package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.solution.NotFoundSolutionException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.dto.response.SolutionResponseDTO;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final TokenService tokenService;

    @Transactional
    public SolutionResponseDTO updateSolution(HttpServletRequest tokenRequest, Long solutionId, SolutionUpdateRequest solutionUpdateRequest) {
        tokenService.validateAccessToken(tokenRequest);
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        solution.update(solutionUpdateRequest);
        return new SolutionResponseDTO(solutionRepository.save(solution));
    }
}
