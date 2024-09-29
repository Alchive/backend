package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.problem.NotFoundProblemException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.algorithmProblem.AlgorithmProblem;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.request.BoardMemoUpdateRequest;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.response.*;
import com.Alchive.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;
    private final SolutionRepository solutionRepository;

    // 게시물 메서드
    @Transactional
    public BoardResponseDTO createBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
        ProblemCreateRequest problemCreateRequest = boardCreateRequest.getProblemCreateRequest();
        // 문제 정보 저장 여부 확인
        if (!problemRepository.existsByNumberAndPlatform(problemCreateRequest.getNumber(), problemCreateRequest.getPlatform())) {
            // 문제가 저장되지 않은 경우
            createProblem(problemCreateRequest);
        }
        Problem problem = problemRepository.findByNumberAndPlatform(problemCreateRequest.getNumber(), problemCreateRequest.getPlatform());
        Board board = Board.of(problem, user, boardCreateRequest);
        return new BoardResponseDTO(boardRepository.save(board));
    }

    public BoardDetailResponseDTO getBoardDetail(Long boardId) {
        // 게시물 정보
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        BoardResponseDTO boardResponseDTO = new BoardResponseDTO(board);

        // 문제 정보
        Long problemId = board.getProblem().getId();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(NotFoundProblemException::new);
        ProblemResponseDTO problemResponseDTO = new ProblemResponseDTO(problem, getProblemAlgorithms(problemId));

        // 풀이 정보
        List<SolutionResponseDTO> solutions = getSolutions(boardId);

        // DTO로 묶어서 반환
        return new BoardDetailResponseDTO(boardResponseDTO, problemResponseDTO, solutions);
    }

    @Transactional
    public BoardResponseDTO updateBoardMemo(Long boardId, BoardMemoUpdateRequest updateRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        board.update(updateRequest.getMemo());
        return new BoardResponseDTO(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        board.softDelete();
        boardRepository.save(board);
    }

    // 문제 메서드
    @Transactional
    public void createProblem(ProblemCreateRequest problemCreateRequest) {
        Problem problem = problemRepository.save(Problem.of(problemCreateRequest));
        List<String> algorithms = problemCreateRequest.getAlgorithms();

        for (String algorithmName : algorithms) {
            // 알고리즘 저장 여부 확인
            if (!algorithmRepository.existsByName(algorithmName)) {
                // 알고리즘이 저장되지 않은 경우
                createAlgorithm(algorithmName);
            }
            Algorithm algorithm = algorithmRepository.findByName(algorithmName);
            // Algorithm - Problem 연결
            AlgorithmProblem algorithmProblem = AlgorithmProblem.of(algorithm, problem);
            algorithmProblemRepository.save(algorithmProblem);
        }
    }

    public List<String> getProblemAlgorithms(Long problemId) {
        List<String> algorithmNames = algorithmProblemRepository.findAlgorithmNamesByProblemId(problemId);
        return algorithmNames;
    }

    // 알고리즘 메서드
    @Transactional
    public void createAlgorithm(String name) {
        Algorithm newAlgorithm = Algorithm.of(name);
        algorithmRepository.save(newAlgorithm);
    }

    public List<SolutionResponseDTO> getSolutions(Long boardId) {
        List<Solution> solutions = solutionRepository.findAllByBoard_Id(boardId);
        return solutions.stream()
                .map(SolutionResponseDTO::new)
                .collect(Collectors.toList());
    }
}
