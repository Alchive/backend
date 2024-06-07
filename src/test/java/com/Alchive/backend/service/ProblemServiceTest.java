package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.problem.NoSuchCategoryException;
import com.Alchive.backend.config.error.exception.problem.NoSuchPlatformException;
import com.Alchive.backend.config.error.exception.problem.NoSuchProblemIdException;
import com.Alchive.backend.config.error.exception.problem.UnAuthorizedUserException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.domain.Solution;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.ProblemMemoUpdateRequest;
import com.Alchive.backend.dto.response.ProblemDetailResponseDTO;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.repository.ProblemRepository;
import com.Alchive.backend.repository.SolutionRepository;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProblemServiceTest {

    @InjectMocks
    private ProblemService problemService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private SolutionService solutionService;

    @Mock
    private AlgorithmService algorithmService;

    @Mock
    private HttpServletRequest request;

    private User user;
    private Problem problem;
    private Solution solution;
    private ProblemCreateRequest problemCreateRequest;
    private List<Algorithm> algorithmList;

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

        problemCreateRequest = ProblemCreateRequest.builder()
                .problemNumber(123)
                .problemTitle("Test Problem")
                .problemUrl("http://example.com")
                .problemDescription("This is a test problem")
                .problemDifficulty("Easy")
                .problemPlatform("Baekjoon")
                .algorithmNames(new ArrayList<>())
                .problemMemo("Test memo")
                .problemState("Unsolved")
                .solutionInfo(
                        ProblemCreateRequest.SolutionInfo.builder()
                                .content(solution.getContent())
                                .code(solution.getCode())
                                .codeLanguage(solution.getCodeLanguage())
                                .codeCorrect(solution.isCodeCorrect())
                                .codeMemory(solution.getCodeMemory())
                                .codeTime(solution.getCodeTime())
                                .build()
                )
                .build();

        Algorithm algorithm1 = Algorithm.builder()
                .algorithmId(1L)
                .algorithmName("Dynamic Programming")
                .build();

        Algorithm algorithm2 = Algorithm.builder()
                .algorithmId(2L)
                .algorithmName("Graph Theory")
                .build();

        algorithmList = List.of(algorithm1, algorithm2);

        // ProblemListResponseDTO 객체 생성
        ProblemListResponseDTO problemListResponseDTO = ProblemListResponseDTO.of(problem, algorithmList);
    }

    @DisplayName("미제출 문제 저장 - 유저를 찾을 수 없음")
    @Test
    void createProblem_userNotFound() {
        // JWT 검사 통과 가정
        doNothing().when(tokenService).validateAccessToken(request); // 만료 여부 통과 가정
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId()); // 토큰에서 사용자ID 추출 과정 모킹

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty()); // 유저 없음 가정

        // 올바른 예외가 발생하는지 확인
        assertThrows(NoSuchUserIdException.class, () -> {
            problemService.createProblem(request, problemCreateRequest);
        });

        // userRepository.findById() 호출 횟수 확인
        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("미제출 문제 저장 - 신규 문제")
    @Test
    void createProblem_newProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user)); // 유저 존재 가정
        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(null); // 신규 문제 가정

        problemService.createProblem(request, problemCreateRequest);

        verify(problemRepository, times(1)).save(any(Problem.class));
        verify(algorithmService, times(1)).saveAlgorihtmNames(any(Problem.class), anyList());
    }

    @DisplayName("미제출 문제 저장 - 기존 문제")
    @Test
    void createProblem_existProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user)); // 유저 존재 가정
        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(problem); // 기존 문제 가정
    }

    @DisplayName("맞/틀 문제 저장 - 유저를 찾을 수 없음")
    @Test
    void createProblemSubmit_userNotFound() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty()); // 빈 유저 가정

        assertThrows(NoSuchUserIdException.class, () -> {
            problemService.createProblemSubmit(request, problemCreateRequest);
        });

        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("맞/틀 문제 저장 - 신규 문제")
    @Test
    void createProblemSubmit_newProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(null); // 신규 문제 가정

        problemService.createProblemSubmit(request, problemCreateRequest);

        verify(problemRepository, times(1)).save(any(Problem.class));
        verify(algorithmService, times(1)).saveAlgorihtmNames(any(Problem.class), anyList());
        verify(solutionService, times(1)).saveSolution(any(), any());
    }

    @DisplayName("맞/틀 문제 저장 - 기존 문제")
    @Test
    void createProblemSubmit_existProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(problem); // 문제 존재함 가정

        problemService.createProblemSubmit(request, problemCreateRequest);

        verify(problemRepository, times(1)).save(any(Problem.class));
        verify(solutionService, times(1)).saveSolution(any(Problem.class), any());
    }

    @DisplayName("문제 저장 여부 검사 - 신규")
    @Test
    void checkProblem_newProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(null); // 신규 가정

        boolean result = problemService.checkProblem(request, 123, "Baekjoon");

        // 존재함이 거짓인지(신규) 확인
        assertFalse(result);
    }

    @DisplayName("문제 저장 여부 검사 - 기존")
    @Test
    void checkProblem_existProblem() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(anyLong(), anyInt(), anyString()))
                .thenReturn(problem); // 존재함 가정

        boolean result = problemService.checkProblem(request, 123, "Baekjoon");

        // 존재함이 참인지(기존) 확인
        assertTrue(result);
    }

    @DisplayName("플랫폼 별 조회 - 성공")
    @Test
    void getProblemsByPlatform_success() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findByUserUserIdAndProblemPlatform(anyLong(), anyString()))
                .thenReturn(List.of(problem));

        List<ProblemListResponseDTO> result = problemService.getProblemsByPlatform(request, "Baekjoon");

//        assertNotNull(result); // 검색 결과가 없어도 성공임
        assertEquals(1, result.size()); // 개수 확인

        ProblemListResponseDTO returnedProblem = result.get(0); // 반환된 문제의 내용이 기대하는 내용과 일치하는지 확인
        assertEquals(problem.getProblemId(), returnedProblem.getProblemId());
        assertEquals(problem.getProblemNumber(), returnedProblem.getProblemNumber());
        assertEquals(problem.getProblemTitle(), returnedProblem.getProblemTitle());
        assertEquals(problem.getProblemDifficulty(), returnedProblem.getProblemDifficulty());
        assertEquals(problem.getProblemPlatform(), returnedProblem.getProblemPlatform());
        assertEquals(problem.getProblemState(), returnedProblem.getProblemState());
    }

    @DisplayName("플랫폼 별 조회 - 카테고리를 찾을 수 없음")
    @Test
    void getProblemsByPlatform_categoryNotFound() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        assertThrows(NoSuchPlatformException.class, () -> {
            problemService.getProblemsByPlatform(request, "UnknownPlatform");
        });
    }

    @DisplayName("문제 검색 - 성공")
    @Test
    void getProblemsSearch_success() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findByUserIdAndProblemNumberOrTitleContaining(anyLong(), anyString()))
                .thenReturn(List.of(problem));

        List<ProblemListResponseDTO> result = problemService.getProblemsSearch(request, "Test", null);

//        assertNotNull(result);
        assertEquals(1, result.size());

        ProblemListResponseDTO returnedProblem = result.get(0); // 반환된 문제의 내용이 기대하는 내용과 일치하는지 확인
        assertEquals(problem.getProblemId(), returnedProblem.getProblemId());
        assertEquals(problem.getProblemNumber(), returnedProblem.getProblemNumber());
        assertEquals(problem.getProblemTitle(), returnedProblem.getProblemTitle());
        assertEquals(problem.getProblemDifficulty(), returnedProblem.getProblemDifficulty());
        assertEquals(problem.getProblemPlatform(), returnedProblem.getProblemPlatform());
        assertEquals(problem.getProblemState(), returnedProblem.getProblemState());
    }

    @DisplayName("문제 검색 - 카테고리를 찾을 수 없음")
    @Test
    void getProblemsSearch_categoryNotFound() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        assertThrows(NoSuchCategoryException.class, () -> {
            problemService.getProblemsSearch(request, "Test", "unknownCategory");
        });
    }

    @DisplayName("사용자가 작성한 전체 목록 조회 - 성공")
    @Test
    void getProblemsByUserId_success() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findByUserUserId(anyLong())).thenReturn(List.of(problem));

        List<ProblemListResponseDTO> result = problemService.getProblemsByUserId(request);

        assertNotNull(result);
        assertEquals(1, result.size());

        ProblemListResponseDTO returnedProblem = result.get(0); // 반환된 문제의 내용이 기대하는 내용과 일치하는지 확인
        assertEquals(problem.getProblemId(), returnedProblem.getProblemId());
        assertEquals(problem.getProblemNumber(), returnedProblem.getProblemNumber());
        assertEquals(problem.getProblemTitle(), returnedProblem.getProblemTitle());
        assertEquals(problem.getProblemDifficulty(), returnedProblem.getProblemDifficulty());
        assertEquals(problem.getProblemPlatform(), returnedProblem.getProblemPlatform());
        assertEquals(problem.getProblemState(), returnedProblem.getProblemState());
    }

    @DisplayName("알고리즘 정보를 추가한 문제 목록 반환 - 성공")
    @Test
    void getProblemListWithAlgorithm_success() {
        when(algorithmService.addAlgorithmList(problem)).thenReturn(algorithmList);

        List<ProblemListResponseDTO> result = problemService.getProblemListWithAlgorithm(List.of(problem));

        // 결과 검증
        assertEquals(1, result.size());

        ProblemListResponseDTO resultProblem = result.get(0);
        assertEquals(problem.getProblemId(), resultProblem.getProblemId());
        assertEquals(problem.getProblemNumber(), resultProblem.getProblemNumber());
        assertEquals(problem.getProblemTitle(), resultProblem.getProblemTitle());
        assertEquals(problem.getProblemDifficulty(), resultProblem.getProblemDifficulty());
        assertEquals(problem.getProblemPlatform(), resultProblem.getProblemPlatform());
        assertEquals(problem.getProblemState(), resultProblem.getProblemState());
        assertEquals(algorithmList, resultProblem.getAlgorithmList());
    }

    @DisplayName("문제 삭제 - 성공")
    @Test
    void deleteProblem_success() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));

        assertDoesNotThrow(() -> problemService.deleteProblem(request, problem.getProblemId())); // 예외가 발생하지 않는지 검증

        verify(problemRepository, times(1)).delete(problem);
    }

    @DisplayName("문제 삭제 - 문제를 찾을 수 없음")
    @Test
    void deleteProblem_problemNotFound() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty()); // 문제 존재하지 않음 가정

        assertThrows(NoSuchProblemIdException.class, () -> {
            problemService.deleteProblem(request, anyLong());
        });
    }

    @DisplayName("문제 삭제 - 작성자 불일치(권한 없음)")
    @Test
    void deleteProblem_unAuthorizedUser() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(2L); // userId 다르게 가정

        when(problemRepository.findById(anyLong())).thenReturn(Optional.of(problem));

        // 권한 없음 예외가 발생하는지 확인
        assertThrows(UnAuthorizedUserException.class, () -> problemService.deleteProblem(request, 1L));

        // 문제가 삭제되지 않았는지 확인
        verify(problemRepository, never()).delete(problem);
    }

    @DisplayName("단일 문제 조회 - 성공")
    @Test
    void getProblemByProblemId_success() {
        when(problemRepository.findById(problem.getProblemId())).thenReturn(Optional.of(problem));
        when(algorithmService.addAlgorithmList(any(Problem.class))).thenReturn(algorithmList);
        when(solutionRepository.findAllByProblemProblemId(problem.getProblemId())).thenReturn(List.of(solution));

        // 문제 조회
        ProblemDetailResponseDTO result = problemService.getProblemByProblemId(problem.getProblemId());

        // 반환된 문제가 기대하는 내용과 일치하는지 확인
        assertEquals(problem.getProblemId(), result.getProblemId());
        assertEquals(problem.getUser().getUserId(), result.getUserId());
        assertEquals(problem.getProblemNumber(), result.getProblemNumber());
        assertEquals(problem.getProblemTitle(), result.getProblemTitle());
        assertEquals(problem.getProblemUrl(), result.getProblemUrl());
        assertEquals(problem.getProblemDescription(), result.getProblemDescription());
        assertEquals(problem.getProblemDifficulty(), result.getProblemDifficulty());
        assertEquals(problem.getProblemPlatform(), result.getProblemPlatform());
        assertEquals(problem.getProblemMemo(), result.getProblemMemo());
        assertEquals(problem.getProblemState(), result.getProblemState());
        assertEquals(problem.getCreatedAt(), result.getCreatedAt());
        assertEquals(problem.getUpdatedAt(), result.getUpdatedAt());
        assertIterableEquals(algorithmList, result.getAlgorithmList());

        assertEquals(1, result.getSolutionList().size());

        ProblemDetailResponseDTO.SolutionInfo solutionInfo = result.getSolutionList().get(0);
        assertEquals(solution.getContent(), solutionInfo.getContent());
        assertEquals(solution.getCode(), solutionInfo.getCode());
        assertEquals(solution.getCodeLanguage(), solutionInfo.getCodeLanguage());
        assertEquals(solution.isCodeCorrect(), solutionInfo.isCodeCorrect());
        assertEquals(solution.getCodeMemory(), solutionInfo.getCodeMemory());
        assertEquals(solution.getCodeTime(), solutionInfo.getCodeTime());
    }

    @DisplayName("단일 문제 조회 - 문제를 찾을 수 없음")
    @Test
    void getProblemByProblemId_problemNotFound() {
        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty()); // 문제가 없는 경우 가정

        assertThrows(NoSuchProblemIdException.class, () -> problemService.getProblemByProblemId(problem.getProblemId()));
    }

    @DisplayName("문제 메모 수정 - 성공")
    @Test
    void updateProblemMemo_success() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findById(anyLong())).thenReturn(Optional.of(problem));

        assertDoesNotThrow(() -> problemService.updateProblemMemo(request, new ProblemMemoUpdateRequest(1L, "New memo")));

        // 문제의 메모가 수정되었는지 확인
        assertEquals("New memo", problem.getProblemMemo());
    }

    @DisplayName("문제 메모 수정 - 문제를 찾을 수 없음")
    @Test
    void updateProblemMemo_problemNotFound() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        when(problemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchProblemIdException.class, () -> problemService.updateProblemMemo(request, new ProblemMemoUpdateRequest(1L, "New memo")));
    }

    @DisplayName("문제 메모 수정 - 작성자 불일치(권한 없음)")
    @Test
    void updateProblemMemo_unAuthorizedUser() {
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(2L);

        when(problemRepository.findById(anyLong())).thenReturn(Optional.of(problem));

        assertThrows(UnAuthorizedUserException.class, () -> problemService.updateProblemMemo(request, new ProblemMemoUpdateRequest(1L, "New memo")));

        assertNotEquals("New memo", problem.getProblemMemo()); // 문제의 메모가 수정되지 않았는지 확인
    }
}