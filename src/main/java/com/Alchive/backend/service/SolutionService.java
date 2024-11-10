package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.solution.NotFoundSolutionException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SolutionCreateRequest;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.dto.response.SolutionDetailResponseDTO;
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
    private final UserService userService;

    public SolutionDetailResponseDTO createSolution(Long boardId, SolutionCreateRequest solutionRequest) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundBoardException::new);
        Solution solution = Solution.of(board, solutionRequest);
        return new SolutionDetailResponseDTO(solutionRepository.save(solution));
    }

    @Transactional
    public SolutionDetailResponseDTO updateSolution(User user, Long solutionId, SolutionUpdateRequest solutionRequest) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        userService.validateUser(user.getId(), solution.getBoard().getUser().getId());
        return new SolutionDetailResponseDTO(solution.update(solutionRequest));
    }

    @Transactional
    public void deleteSolution(User user, Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId).orElseThrow(NotFoundSolutionException::new);
        userService.validateUser(user.getId(), solution.getBoard().getUser().getId());
        solutionRepository.delete(solution);
    }
}
