package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.token.UnmatchedUserIdException;
import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.algorithmProblem.AlgorithmProblem;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.board.BoardStatus;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.problem.ProblemDifficulty;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.request.BoardMemoUpdateRequest;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.ProblemNumberRequest;
import com.Alchive.backend.dto.response.BoardDetailResponseDTO;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @InjectMocks
    private BoardService sut;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ProblemRepository problemRepository;
    @Mock
    private AlgorithmRepository algorithmRepository;
    @Mock
    private AlgorithmProblemRepository algorithmProblemRepository;
    @Mock
    private SolutionRepository solutionRepository;
    @Mock
    private UserService userService;

    private Board board;
    private Problem problem;
    private User user;
    private List<String> algorithms = Arrays.asList("BFS", "DFS");

    private long problemId = 1L;
    private int problemNumber = 1;
    private String problemTitle = "testProblem";
    private String problemContent = "this is test problem's content";
    private String problemUrl = "https://test/problem/1";
    private ProblemDifficulty problemDifficulty = ProblemDifficulty.LEVEL0;
    private ProblemPlatform problemPlatform = ProblemPlatform.BAEKJOON;
    private long userId = 1L;
    private String userEmail = "testUser@test.com";
    private String userName = "testUsername";
    private long boardId = 1L;
    private String boardMemo = "test problem's memo";
    private BoardStatus boardStatus = BoardStatus.CORRECT;
    private String boardDescription = "test problem's description";
    @BeforeEach

    public void setUp() {
        problem = new Problem(problemId, problemNumber, problemTitle, problemContent, problemUrl, problemDifficulty, problemPlatform);
        user = new User(userId, userEmail, userName);
        board = new Board(boardId, problem, user, boardMemo, boardStatus, boardDescription);
    }

//    @DisplayName("게시물, 문제, 풀이 묶어서 반환 - 성공")
//    @Test
//    public void toBoardSolutionDtoSuccess() {
//        when(problemRepository.findById(problemId)).thenReturn(Optional.ofNullable(problem));
//        when(algorithmProblemRepository.findAlgorithmNamesByProblemId(problemId)).thenReturn(algorithms);
//        when(solutionRepository.findAllByBoard_Id(boardId)).thenReturn(new ArrayList<>());
//    }

    @DisplayName("문제 저장 여부 - 참")
    @Test
    public void isBoardSavedTrue() {
        when(boardRepository.findByProblem_PlatformAndProblem_NumberAndUser_Id(problemPlatform, problemNumber, userId)).thenReturn(Optional.ofNullable(board));

        when(problemRepository.findById(problemId)).thenReturn(Optional.ofNullable(problem));
        when(algorithmProblemRepository.findAlgorithmNamesByProblemId(problemId)).thenReturn(algorithms);
        when(solutionRepository.findAllByBoard_Id(boardId)).thenReturn(new ArrayList<>());
        ProblemNumberRequest request = new ProblemNumberRequest(problemPlatform, problemNumber);

        BoardDetailResponseDTO result = sut.isBoardSaved(user, request);

        Assertions.assertEquals(boardMemo, result.getBoard().getMemo());
        Assertions.assertEquals(0, result.getSolutions().size());
        Assertions.assertEquals(2, result.getProblem().getAlgorithms().size());
    }

    @DisplayName("문제 저장 여부 - 거짓")
    @Test
    public void isBoardSavedFalse() {
        when(boardRepository.findByProblem_PlatformAndProblem_NumberAndUser_Id(problemPlatform, problemNumber, userId)).thenReturn(Optional.empty());
        ProblemNumberRequest request = new ProblemNumberRequest(problemPlatform, problemNumber);

        BoardDetailResponseDTO result = sut.isBoardSaved(user, request);

        Assertions.assertEquals(null, result);
    }

    @DisplayName("게시물 저장 - 이미 존재하는 문제")
    @Test
    public void createBoardWithExistProblem() {
        when(problemRepository.existsByNumberAndPlatform(problemNumber, problemPlatform)).thenReturn(true);
        when(problemRepository.findByNumberAndPlatform(problemNumber, problemPlatform)).thenReturn(problem);
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        ProblemCreateRequest request1 = new ProblemCreateRequest(problemNumber, problemTitle, problemContent, problemUrl, problemDifficulty, problemPlatform, algorithms);
        BoardCreateRequest request = new BoardCreateRequest(request1, boardMemo, boardDescription, boardStatus);

        BoardResponseDTO result = sut.createBoard(user, request);

        Assertions.assertEquals(boardMemo, result.getMemo());
        Assertions.assertEquals(boardStatus, result.getStatus());
        Assertions.assertEquals(boardDescription, result.getDescription());
    }

    @DisplayName("문제 생성 - 성공")
    @Test
    public void createProblemSuccess() {
        when(problemRepository.save(any(Problem.class))).thenReturn(problem);
        when(algorithmRepository.save(any(Algorithm.class))).thenReturn(Algorithm.of("algorithm"));
        when(algorithmRepository.findByName(any(String.class))).thenReturn(Algorithm.of("algorithm"));
        AlgorithmProblem algorithmProblem = new AlgorithmProblem(1L, Algorithm.of("algorithm"), problem);
        when(algorithmProblemRepository.save(any(AlgorithmProblem.class))).thenReturn(algorithmProblem);
        ProblemCreateRequest request = new ProblemCreateRequest(problemNumber, problemTitle, problemContent, problemUrl, problemDifficulty, problemPlatform, algorithms);

        sut.createProblem(request);
    }

    @DisplayName("게시물 수정 - 작성자가 수정")
    @Test
    public void updateBoardByAuthor() {
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));
        doNothing().when(userService).validateUser(any(Long.class), any(Long.class));
        String newMemo = "new Memo";
        BoardMemoUpdateRequest request = new BoardMemoUpdateRequest(newMemo);

        sut.updateBoardMemo(user, boardId, request);
    }

    @DisplayName("게시물 수정 - 작성자가 아닌 사람이 수정")
    @Test
    public void updateBoardByNonAuthor() {
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(board));
        doCallRealMethod().when(userService).validateUser(anyLong(), anyLong());
        User newUser = new User(2L, "newUser@test.com", "newUser");
        BoardMemoUpdateRequest request = new BoardMemoUpdateRequest("new Memo");

        Assertions.assertThrows(UnmatchedUserIdException.class, () -> sut.updateBoardMemo(newUser, boardId, request));
    }
}
