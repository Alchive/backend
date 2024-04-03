package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.config.exception.NoSuchUserException;
import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.ProblemRepository;
import com.Alchive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;

    // 플랫폼 별 조회
    public List<ProblemListResponseDTO> getProblemsByPlatform(Long userId, String platform) {
        // userId가 db에 존재하지 않을 경우
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchUserException(Code.USER_NOT_FOUND, userId);
        }
        // Baekjoon, Programmers, Leetcode가 아닌 경우
        if (!platform.equals("Baekjoon") && !platform.equals("Programmers") && !platform.equals("Leetcode")) {
            throw new NoSuchPlatformException(Code.PLATFORM_INVALID, platform);
        }
        // problem 테이블에서 플랫폼으로 문제 조회
        List<Problem> problems = problemRepository.findByUserUserIdAndProblemPlatform(userId, platform);

        // 문제 리스트를 보내서 알고리즘 이름 추가하기
        return addAlgorithm(problems);
    }

    // 문제 검색
    public List<ProblemListResponseDTO> getProblemsSearch(Long userId, String keyword, String category) {
        // userId가 db에 존재하지 않을 경우
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchUserException(Code.USER_NOT_FOUND, userId);
        }
        List<Problem> problems = new ArrayList<>();
        if (category == null) {
            // 문제 번호 검색과 제목 검색에서의 중복을 제거하기 위한 해시세트 사용
            Set<Problem> uniqueProblems = new HashSet<>();
            List<Problem> numbers = problemRepository.findByUserIdAndProblemNumberContaining(userId, keyword);
            List<Problem> titles = problemRepository.findByUserIdAndProblemTitleContaining(userId, keyword);
            uniqueProblems.addAll(numbers);
            uniqueProblems.addAll(titles);
            problems = List.copyOf(uniqueProblems);
        } else {
            if ("number".equals(category)) {
                problems = problemRepository.findByUserIdAndProblemNumberContaining(userId, keyword);
            } else if ("title".equals(category)) {
                problems = problemRepository.findByUserIdAndProblemTitleContaining(userId, keyword);
            } else {
                throw new NoSuchPlatformException(Code.CATEGORY_INVALID, category);
            }
        }

        return addAlgorithm(problems);
    }

    // 알고리즘 배열 추가하기 - 전체 목록 조회 & 플랫폼 별 조회 & 문제 검색
    public List<ProblemListResponseDTO> addAlgorithm(List<Problem> problems) {
        List<ProblemListResponseDTO> problemListDataList = new ArrayList<>();

        for (Problem problem : problems) {
            ProblemListResponseDTO problemData = new ProblemListResponseDTO();
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

}
