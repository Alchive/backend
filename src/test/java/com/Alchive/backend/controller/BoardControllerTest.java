package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.board.BoardStatus;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.problem.ProblemDifficulty;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.ProblemNumberRequest;
import com.Alchive.backend.dto.response.BoardDetailResponseDTO;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.dto.response.ProblemResponseDTO;
import com.Alchive.backend.service.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.Alchive.backend.config.result.ResultCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {
    @InjectMocks
    private BoardController sut;
    @Mock
    private BoardService boardService;
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

    @DisplayName("게시물 저장 여부 조회 - 이미 존재하는 문제")
    @Test
    public void existBoard() {
        BoardDetailResponseDTO response = new BoardDetailResponseDTO(new BoardResponseDTO(board), new ProblemResponseDTO(problem, algorithms), new ArrayList<>());
        when(boardService.isBoardSaved(any(User.class), any(ProblemNumberRequest.class))).thenReturn(response);
        when(boardService.boardSavedStatus(response)).thenReturn(BOARD_ALREADY_EXIST);

        ResponseEntity<ResultResponse> result = sut.isBoardSaved(user, new ProblemNumberRequest(problemPlatform, problemNumber));

        Assertions.assertEquals(BOARD_ALREADY_EXIST.getMessage(), result.getBody().getMessage());
    }

    @DisplayName("게시물 저장 여부 조회 - 존재하지 않는 문제")
    @Test
    public void newBoard() {
        when(boardService.isBoardSaved(any(User.class), any(ProblemNumberRequest.class))).thenReturn(null);
        when(boardService.boardSavedStatus(nullable(BoardDetailResponseDTO.class))).thenReturn(BOARD_NOT_EXIST);

        ResponseEntity<ResultResponse> result = sut.isBoardSaved(user, new ProblemNumberRequest(problemPlatform, problemNumber));

        Assertions.assertEquals(BOARD_NOT_EXIST.getMessage(), result.getBody().getMessage());
    }
}
