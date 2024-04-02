package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.dto.response.ProblemListDTO;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    private final AlgorithmProblemRepository algorithmProblemRepository;

    // 플랫폼 별 조회
    public List<ProblemListDTO> getProblemsByPlatform(String platform) {
        // Baekjoon, Programmers, Leetcde가 아닌 경우
//        if (!platform.equals("Baekjoon") && !platform.equals("Programmers") && !platform.equals("Leetcode")) {
//            throw new NoSuchPlatformException(Code.PLATFORM_INVALID, platform);
//        }
        // problem 테이블에서 플랫폼으로 문제 조회
        List<Problem> problems = problemRepository.findByProblemPlatform(platform);
        if (problems.isEmpty()) {
            throw new NoSuchPlatformException(Code.PROBLEM_NOT_FOUND, platform);
        }

        return addAlgorithm(problems);
    }

    // 알고리즘 배열 추가하기 - 전체 목록 조회 & 플랫폼 별 조회
    public List<ProblemListDTO> addAlgorithm(List<Problem> problems) {
        List<ProblemListDTO> problemListDataList = new ArrayList<>();

        for (Problem problem : problems) {
            ProblemListDTO problemData = new ProblemListDTO();
            problemData.setProblemId(problem.getProblemId());
            problemData.setProblemNumber(problem.getProblemNumber());
            problemData.setProblemTitle(problem.getProblemTitle());
            problemData.setProblemDifficulty(problem.getProblemDifficulty());
            problemData.setProblemPlatform(problem.getProblemPlatform());
            problemData.setProblemState(problem.getProblemState());

            List<String> algorithmNames = new ArrayList<>();
            List<AlgorithmProblem> algorithmProblems = algorithmProblemRepository.findByProblem(problem);
            for (AlgorithmProblem algorithmProblem : algorithmProblems) {
                Algorithm algorithm = algorithmProblem.getAlgorithm();
                algorithmNames.add(algorithm.getAlgorithmName());
            }
            problemData.setAlgorithmName(algorithmNames);

            problemListDataList.add(problemData);
        }

        return problemListDataList;
    }


    // 모든 문제 조회
    public List<ProblemListDTO> getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        return convertToDTOList(problems);
    }

    // Problem 엔티티를 ProblemListDTO로 변환하는 메서드
    private List<ProblemListDTO> convertToDTOList(List<Problem> problems) {
        List<ProblemListDTO> dtoList = new ArrayList<>();
        for (Problem problem : problems) {
            ProblemListDTO dto = new ProblemListDTO();
            dto.setProblemId(problem.getProblemId());
            dto.setProblemNumber(problem.getProblemNumber());
            dto.setProblemTitle(problem.getProblemTitle());
            dto.setProblemDifficulty(problem.getProblemDifficulty());
            dto.setProblemPlatform(problem.getProblemPlatform());
            dto.setProblemState(problem.getProblemState());
            // 알고리즘 정보는 여기서 추가할 수도 있습니다.
            // dto.setAlgorithmName(problem.getAlgorithmName()); // 예시일 뿐 실제 코드에는 맞게 수정해야 합니다.
            dtoList.add(dto);
        }
        return dtoList;
    }
}
