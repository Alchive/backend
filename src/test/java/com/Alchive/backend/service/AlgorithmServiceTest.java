package com.Alchive.backend.service;

import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.AlgorithmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlgorithmServiceTest {

    @InjectMocks
    private AlgorithmService algorithmService;

    @Mock
    private AlgorithmRepository algorithmRepository;

    @Mock
    private AlgorithmProblemRepository algorithmProblemRepository;

    private User user;
    private Problem problem;
    private Algorithm algorithm;
    private AlgorithmProblem algorithmProblem;
    private List<String> algorithmNames;

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

        algorithm = Algorithm.builder()
                .algorithmId(1L)
                .algorithmName("Dynamic Programming")
                .build();

        algorithmNames = List.of(algorithm.getAlgorithmName());

        algorithmProblem = AlgorithmProblem.builder()
                .algoProId(1L)
                .algorithm(algorithm)
                .problem(problem)
                .build();

    }

    @DisplayName("알고리즘 배열 추가하기")
    @Test
    void addAlgorithmList() {
        when(algorithmProblemRepository.findAllByProblemProblemId(problem.getProblemId()))
                .thenReturn(List.of(algorithmProblem));

        List<Algorithm> result = algorithmService.addAlgorithmList(problem);

        assertEquals(1, result.size());
        Algorithm returnedAlgorithm = result.get(0);
        assertEquals(returnedAlgorithm.getAlgorithmId(), algorithm.getAlgorithmId());
        assertEquals(returnedAlgorithm.getAlgorithmName(), algorithm.getAlgorithmName());
    }

    @DisplayName("알고리즘 저장 - 신규 알고리즘")
    @Test
    void saveAlgorihtmNames_newAlgorithm() {
        when(algorithmRepository.existsByAlgorithmName(algorithm.getAlgorithmName())).thenReturn(false); // 신규 가정

        algorithmService.saveAlgorihtmNames(problem, algorithmNames);

        verify(algorithmRepository, times(1)).save(any(Algorithm.class));
        verify(algorithmProblemRepository, times(1)).save(any());
    }

    @DisplayName("알고리즘 저장 - 기존 알고리즘")
    @Test
    void saveAlgorihtmNames_existAlgorithm() {
        when(algorithmRepository.existsByAlgorithmName(algorithm.getAlgorithmName())).thenReturn(true); // 존재함 가정

        algorithmService.saveAlgorihtmNames(problem, algorithmNames);

        verify(algorithmRepository, never()).save(any()); // 알고리즘 저장이 일어나지 않아야함
        verify(algorithmProblemRepository, times(1)).save(any());
    }
}