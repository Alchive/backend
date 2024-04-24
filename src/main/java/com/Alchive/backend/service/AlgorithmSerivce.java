package com.Alchive.backend.service;

import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.AlgorithmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmSerivce {

    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;

    // 알고리즘 배열 추가하기 - 아이디와 이름을 함께 반환
    public List<Algorithm> addAlgorithmList(Problem problem) {
        List<Algorithm> algorithmList = new ArrayList<>();
        List<AlgorithmProblem> algorithmProblems = algorithmProblemRepository.findAllByProblemProblemId(problem.getProblemId());
        for (AlgorithmProblem algorithmProblem : algorithmProblems) {
            Algorithm algorithm = Algorithm.builder()
                    .algorithmId(algorithmProblem.getAlgorithm().getAlgorithmId())
                    .algorithmName(algorithmProblem.getAlgorithm().getAlgorithmName())
                    .build();
            algorithmList.add(algorithm);
        }
        return algorithmList;
    }

    // 알고리즘 저장
    public void saveAlgorihtmNames(Problem problem, List<String> algorithmNames) {
        log.info("알고리즘 저장 함수 호출");
        for (String algorithmName : algorithmNames) {
            // 알고리즘 존재 여부 확인
            if (!algorithmRepository.existsByAlgorithmName(algorithmName)) {
                // 존재하지 않는 알고리즘인 경우: 저장
                Algorithm algorithm = Algorithm.builder()
                        .algorithmName(algorithmName)
                        .build();
                algorithmRepository.save(algorithm);
            }
            // 알고리즘-문제 정보 저장
            Algorithm algorithm = algorithmRepository.findByAlgorithmName(algorithmName);
            AlgorithmProblem algorithmProblem = AlgorithmProblem.builder()
                    .algorithm(algorithm)
                    .problem(problem)
                    .build();
            algorithmProblemRepository.save(algorithmProblem);
        }
    }



}
