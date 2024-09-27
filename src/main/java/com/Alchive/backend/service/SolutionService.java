package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.solution.NotFoundSolutionException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.dto.request.SolutionRequest;
import com.Alchive.backend.dto.response.SolutionResponseDTO;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final BoardRepository boardRepository;

    public SolutionResponseDTO createSolution(Long boardId, SolutionRequest solutionRequest) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundBoardException::new);
        Solution solution = Solution.of(board, solutionRequest);
        return new SolutionResponseDTO(solutionRepository.save(solution));
    }

    @Transactional
    public SolutionResponseDTO updateSolution(Long solutionId, SolutionRequest solutionRequest) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        solution.update(solutionRequest);
        return new SolutionResponseDTO(solutionRepository.save(solution));
    }

    @Transactional
    public SolutionResponseDTO deleteSolution(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        solution.softDelete();
        return new SolutionResponseDTO(solutionRepository.save(solution));
    }
}
