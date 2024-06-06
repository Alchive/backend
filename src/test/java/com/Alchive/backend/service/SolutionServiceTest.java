package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.solution.NoSuchSolutionIdException;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.domain.Solution;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.repository.SolutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolutionServiceTest {

    @InjectMocks
    private SolutionService solutionService;

    @Mock
    private SolutionRepository solutionRepository;

    private User user;
    private Problem problem;
    private Solution solution;
    private ProblemCreateRequest.SolutionInfo solutionInfo;
    private SolutionUpdateRequest solutionUpdateRequest;


    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        user = new User(1L);

        problem = Problem.builder()
                .user(user)
                .problemNumber(123)
                .problemTitle("Test Problem")
                .problemUrl("http://example.com")
                .problemDescription("This is a test problem")
                .problemDifficulty("Easy")
                .problemPlatform("Baekjoon")
                .problemMemo("Test memo")
                .problemState("Unsolved")
                .build();

        solution = Solution.builder()
                .problem(problem)
                .content("Solution content")
                .code("Solution code")
                .codeLanguage("Java")
                .codeCorrect(true)
                .codeMemory("4MB")
                .codeTime("10ms")
                .build();

        solutionInfo = ProblemCreateRequest.SolutionInfo.builder()
                                .content(solution.getContent())
                                .code(solution.getCode())
                                .codeLanguage(solution.getCodeLanguage())
                                .codeCorrect(solution.isCodeCorrect())
                                .codeMemory(solution.getCodeMemory())
                                .codeTime(solution.getCodeTime())
                                .build();

        solutionUpdateRequest = SolutionUpdateRequest.builder()
                .solutionId(1L)
                .content("New Solution")
                .build();
    }

    @DisplayName("풀이 저장")
    @Test
    void saveSolution() {
        assertDoesNotThrow(() -> solutionService.saveSolution(problem, solutionInfo));

        verify(solutionRepository, times(1)).save(any(Solution.class));
    }

    @DisplayName("풀이 수정 - 성공")
    @Test
    void updateSolution_success() {
        when(solutionRepository.findBySolutionId(anyLong())).thenReturn(Optional.of(solution));

        assertDoesNotThrow(() -> solutionService.updateSolution(solutionUpdateRequest));

        assertEquals("New Solution", solutionUpdateRequest.getContent());
    }

    @DisplayName("풀이 수정 - 풀이를 찾을 수 없음")
    @Test
    void updateSolution_solutionNotFound() {
        when(solutionRepository.findBySolutionId(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchSolutionIdException.class, () -> solutionService.updateSolution(solutionUpdateRequest));
    }
}