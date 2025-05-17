package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.problem.NotFoundProblemException;
import com.Alchive.backend.config.result.ResultCode;
import com.Alchive.backend.config.result.ResultResponse;
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

    public BoardDetailResponseDTO isBoardSaved(User user, ProblemNumberRequest problemNumberRequest) {
        Optional<Board> board = boardRepository.findByProblem_PlatformAndProblem_NumberAndUser_Id(problemNumberRequest.getPlatform(), problemNumberRequest.getProblemNumber(), user.getId());
        return board.map(this::toBoardDetailResponseDTO).orElse(null);
    }

    public ResultCode boardSavedStatus(BoardDetailResponseDTO board) {
        if (board != null) {
            return ResultCode.BOARD_ALREADY_EXIST;
        }
        return ResultCode.BOARD_NOT_EXIST;
    }

    public Page<List<BoardDetailResponseDTO>> getBoardList(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Board> boardPage = boardRepository.findAll(pageable);

        List<BoardDetailResponseDTO> boardList = boardPage.getContent().stream()
                .map(this::toBoardDetailResponseDTO)
                .toList();

        return new PageImpl<>(List.of(boardList), pageable, boardPage.getTotalElements());
    }

    public BoardDetailResponseDTO getBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        return toBoardDetailResponseDTO(board);
    }

    @Transactional
    public BoardResponseDTO createBoard(User user, BoardCreateRequest boardCreateRequest) {
        ProblemCreateRequest problemCreateRequest = boardCreateRequest.getProblemCreateRequest();
        createProblem(problemCreateRequest);
        Problem problem = problemRepository.findByNumberAndPlatform(problemCreateRequest.getNumber(), problemCreateRequest.getPlatform());
        Board board = Board.of(problem, user, boardCreateRequest);
        return new BoardResponseDTO(boardRepository.save(board));
    }

    @Transactional
    public BoardResponseDTO updateBoard(User user, Long boardId, BoardUpdateRequest updateRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        return new BoardResponseDTO(board.updateDescription(updateRequest.getDescription()));
    }

    @Transactional
    public BoardResponseDTO updateBoardMemo(User user, Long boardId, BoardMemoUpdateRequest updateRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        return new BoardResponseDTO(board.updateMemo(updateRequest.getMemo()));
    }

    @Transactional
    public void deleteBoard(User user, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(NotFoundBoardException::new);
        userService.validateUser(user.getId(), board.getUser().getId());
        boardRepository.delete(board);
    }

    private BoardDetailResponseDTO toBoardDetailResponseDTO(Board board) {
        BoardResponseDTO boardResponseDTO = new BoardResponseDTO(board);
        ProblemResponseDTO problemResponseDTO = getBoardProblem(board);
        List<SolutionResponseDTO> solutions = getBoardSolutions(board.getId());

        return new BoardDetailResponseDTO(boardResponseDTO, problemResponseDTO, solutions);
    }

    private ProblemResponseDTO getBoardProblem(Board board) {
        Long problemId = board.getProblem().getId();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(NotFoundProblemException::new);
        return new ProblemResponseDTO(problem, getProblemAlgorithms(problemId));
    }

    public List<SolutionResponseDTO> getBoardSolutions(Long boardId) {
        List<Solution> solutions = solutionRepository.findAllByBoard_Id(boardId);
        return solutions.stream()
                .map(SolutionResponseDTO::new)
                .toList();
    }

    public List<String> getProblemAlgorithms(Long problemId) {
        return algorithmProblemRepository.findAlgorithmNamesByProblemId(problemId);
    }

    @Transactional
    public void createProblem(ProblemCreateRequest problemCreateRequest) {
        if (problemRepository.existsByNumberAndPlatform(problemCreateRequest.getNumber(), problemCreateRequest.getPlatform())) {
            return;
        }
        Problem problem = problemRepository.save(Problem.of(problemCreateRequest));
        connectProblemAlgorithm(problem, problemCreateRequest.getAlgorithms());
    }

    private void connectProblemAlgorithm(Problem problem, List<String> algorithms) {
        for (String algorithmName : algorithms) {
            createAlgorithm(algorithmName);
            createAlgorithmProblem(algorithmName, problem);
        }
    }

    private void createAlgorithm(String algorithmName) {
        if (algorithmRepository.existsByName(algorithmName)) {
            return;
        }
        Algorithm newAlgorithm = Algorithm.of(algorithmName);
        algorithmRepository.save(newAlgorithm);
    }

    private void createAlgorithmProblem(String algorithmName, Problem problem) {
        Algorithm algorithm = algorithmRepository.findByName(algorithmName);
        AlgorithmProblem algorithmProblem = AlgorithmProblem.of(algorithm, problem);
        algorithmProblemRepository.save(algorithmProblem);
    }
}
