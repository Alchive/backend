package com.Alchive.backend.service;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.solution.NoSuchSolutionIdException;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.domain.Solution;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;

    public void saveSolution(Problem problem, ProblemCreateRequest.SolutionInfo request) {
        Solution solution = Solution.builder()
                .problem(problem)
                .content(request.getContent())
                .code(request.getCode())
                .codeLanguage(request.getCodeLanguage())
                .codeCorrect(request.isCodeCorrect())
                .codeMemory(request.getCodeMemory())
                .codeTime(request.getCodeTime())
                .build();
        solutionRepository.save(solution);
    }

    @Transactional
    public void updateSolution(SolutionUpdateRequest request) {
        Long solutionId = request.getSolutionId();
        String content = request.getContent();
        Solution code = solutionRepository.findBySolutionId(solutionId)
                .orElseThrow(() -> new NoSuchSolutionIdException(solutionId));
        code.update(content);
    }
}
