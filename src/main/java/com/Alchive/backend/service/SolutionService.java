package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.solution.NotFoundSolutionException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.dto.request.SolutionRequest;
import com.Alchive.backend.dto.response.SolutionDetailResponseDTO;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final BoardRepository boardRepository;
    private final TokenService tokenService;

    public SolutionDetailResponseDTO createSolution(HttpServletRequest tokenRequest, Long boardId, SolutionRequest solutionRequest) {
        tokenService.validateAccessToken(tokenRequest);
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundBoardException::new);
        Solution solution = Solution.of(board, solutionRequest);
        return new SolutionDetailResponseDTO(solutionRepository.save(solution));
    }

    @Transactional
    public SolutionDetailResponseDTO updateSolution(HttpServletRequest tokenRequest, Long solutionId, SolutionRequest solutionRequest) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        tokenService.validateUser(userId, solution.getBoard().getUser().getId());
        solution.update(solutionRequest);
        return new SolutionDetailResponseDTO(solutionRepository.save(solution));
    }

    @Transactional
    public SolutionDetailResponseDTO deleteSolution(HttpServletRequest tokenRequest, Long solutionId) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        tokenService.validateUser(userId, solution.getBoard().getUser().getId());
        solution.softDelete();
        return new SolutionDetailResponseDTO(solutionRepository.save(solution));
    }
}
