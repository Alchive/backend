package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.board.NotFoundBoardException;
import com.Alchive.backend.config.error.exception.solution.NotFoundSolutionException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.solution.SolutionLanguage;
import com.Alchive.backend.domain.solution.SolutionStatus;
import com.Alchive.backend.dto.request.SolutionRequest;
import com.Alchive.backend.dto.response.SolutionDetailResponseDTO;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SolutionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SolutionServiceTest {
    @InjectMocks
    private SolutionService solutionService;

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private HttpServletRequest request;

    private Board mockBoard;
    private Solution mockSolution;
    private SolutionRequest mockRequest;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // Mock 객체 초기화
        mockBoard = Board.builder()
                .id(1L)
                .build();

        mockSolution = Solution.builder()
                .id(1L)
                .board(mockBoard)
                .build();

        mockRequest = SolutionRequest.builder()
                .content("Sample content")
                .language(SolutionLanguage.JAVA)
                .description("Sample description")
                .status(SolutionStatus.CORRECT)
                .memory(128)
                .time(200)
                .submitAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("풀이 생성 성공")
    void testCreateSolution() {
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard)); // 게시물이 존재하는 경우
        when(solutionRepository.save(any(Solution.class))).thenReturn(mockSolution); // Solution 객체 저장 시 Mock 객체 반환

        SolutionDetailResponseDTO response = solutionService.createSolution(request, 1L, mockRequest);

        assertNotNull(response); // 응답이 null이 아님을 확인
        assertEquals(mockSolution.getId(), response.getId()); // ID가 일치하는지 확인
        verify(boardRepository, times(1)).findById(1L); // 게시물 조회가 1회 발생했는지 확인
        verify(solutionRepository, times(1)).save(any(Solution.class)); // 저장 메서드가 1회 호출되었는지 확인
    }

    @Test
    @DisplayName("풀이 생성 실패 - 존재하지 않는 게시물")
    void testCreateSolution_BoardNotFound() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty()); // 게시물이 존재하지 않는 경우

        assertThrows(NotFoundBoardException.class, () -> { // NotFoundBoardException이 발생하는지 검증
            solutionService.createSolution(request, 1L, mockRequest);
        });
    }

    @Test
    @DisplayName("풀이 수정 성공")
    void testUpdateSolution() {
        when(solutionRepository.findById(1L)).thenReturn(Optional.of(mockSolution));
        when(solutionRepository.save(any(Solution.class))).thenReturn(mockSolution);

        SolutionDetailResponseDTO response = solutionService.updateSolution(request, 1L, mockRequest);

        assertNotNull(response);
        assertEquals(mockSolution.getId(), response.getId());
        verify(solutionRepository, times(1)).findById(1L);
        verify(solutionRepository, times(1)).save(any(Solution.class));
    }

    @Test
    @DisplayName("풀이 수정 실패 - 존재하지 않는 풀이")
    void testUpdateSolution_NotFound() {
        when(solutionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundSolutionException.class, () -> {
            solutionService.updateSolution(request, 1L, mockRequest);
        });
    }

    @Test
    @DisplayName("풀이 삭제 성공")
    void testDeleteSolution() {
        when(solutionRepository.findById(1L)).thenReturn(Optional.of(mockSolution));
        when(solutionRepository.save(any(Solution.class))).thenReturn(mockSolution);

        SolutionDetailResponseDTO response = solutionService.deleteSolution(request, 1L);

        assertNotNull(response);
        assertEquals(mockSolution.getId(), response.getId());
        assertTrue(mockSolution.getIsDeleted()); // softDelete가 제대로 동작했는지 확인
        verify(solutionRepository, times(1)).findById(1L);
        verify(solutionRepository, times(1)).save(any(Solution.class));
    }

    @Test
    @DisplayName("풀이 삭제 실패 - 존재하지 않는 풀이")
    void testDeleteSolution_NotFound() {
        when(solutionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundSolutionException.class, () -> {
            solutionService.deleteSolution(request, 1L);
        });
    }
}