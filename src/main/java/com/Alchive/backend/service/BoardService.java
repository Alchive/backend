package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.problem.NotFoundProblemException;
import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.algorithmProblem.AlgorithmProblem;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.*;
import com.Alchive.backend.dto.response.BoardDetailResponseDTO;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.dto.response.ProblemResponseDTO;
import com.Alchive.backend.dto.response.SolutionResponseDTO;
import com.Alchive.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ProblemRepository problemRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;
    private final SolutionRepository solutionRepository;
    private final UserService userService;

    private BoardDetailResponseDTO toBoardDetailResponseDTO(Board board) {
        BoardResponseDTO boardResponseDTO = new BoardResponseDTO(board);

        // 문제 정보
        Long problemId = board.getProblem().getId();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(NotFoundProblemException::new);
        ProblemResponseDTO problemResponseDTO = new ProblemResponseDTO(problem, getProblemAlgorithms(problemId));

        // 풀이 정보
        List<SolutionResponseDTO> solutions = getSolutions(board.getId());

        // DTO로 묶어서 반환
        return new BoardDetailResponseDTO(boardResponseDTO, problemResponseDTO, solutions);
    }

    // Board 저장 여부 구현
    public BoardDetailResponseDTO isBoardSaved(User user, ProblemNumberRequest problemNumberRequest) {
        Optional<Board> board = boardRepository.findByProblem_PlatformAndProblem_NumberAndUser_Id(problemNumberRequest.getPlatform(), problemNumberRequest.getProblemNumber(), user.getId());
        return board.map(this::toBoardDetailResponseDTO).orElse(null);
    }

    public Page<List<BoardDetailResponseDTO>> getBoardList(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // Board를 BoardDetailResponseDTO로 변환
        List<BoardDetailResponseDTO> boardList = boardPage.getContent().stream()
                .map(this::toBoardDetailResponseDTO)
                .toList();

        // 변환된 리스트를 새로운 Page 객체로 감싸서 반환
        return new PageImpl<>(List.of(boardList), pageable, boardPage.getTotalElements());
    }

    // 게시물 메서드
    @Transactional
    public BoardResponseDTO createBoard(User user, BoardCreateRequest boardCreateRequest) {
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
        return toBoardDetailResponseDTO(board);
    }

    @Transactional
    public BoardResponseDTO updateBoard(User user, Long boardId, BoardUpdateRequest updateRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        return new BoardResponseDTO(board.updateDescription(updateRequest.getDescription()));
    }

    @Transactional
    public void deleteBoard(User user, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        boardRepository.delete(board);
    }

    @Transactional
    public BoardResponseDTO updateBoardMemo(User user, Long boardId, BoardMemoUpdateRequest updateRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        return new BoardResponseDTO(board.updateMemo(updateRequest.getMemo()));
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
        return algorithmProblemRepository.findAlgorithmNamesByProblemId(problemId);
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
                .toList();
    }
}
