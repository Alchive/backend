package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.domain.*;
import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemDetailResponseDTO;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.ProblemRepository;
import com.Alchive.backend.repository.SolutionRepository;
import com.Alchive.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@RequiredArgsConstructor
@Service
@Repository
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;
    private ProblemService problemService;
    @Autowired
    private SolutionRepository solutionRepository;

    // 플랫폼 별 조회
    public List<ProblemListResponseDTO> getProblemsByPlatform(Long userId, String platform) {
        // userId가 db에 존재하지 않을 경우
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
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
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
        }
//        List<Problem> problems = new ArrayList<>();
        List<Problem> problems;
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

    // 사용자가 작성한 문제 조회
    public List<ProblemListResponseDTO> getProblemsByUserId(Long userId) {
        List<Problem> userProblems = problemRepository.findByUserUserId(userId);
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
        }
        return addAlgorithm(userProblems);
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

    // 문제 삭제
    @Transactional
    public void deleteProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));
        problemRepository.delete(problem);
    }


    // 단일 문제 조회
    public ProblemDetailResponseDTO getProblemByProblemId(Long userId, Long problemId) {
        // userId가 db에 존재하지 않을 경우
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
        }

        // problemId로 문제를 찾음
        Optional<Problem> optionalProblem = problemRepository.findByUserUserIdAndProblemId(userId, problemId);
        Problem problem = optionalProblem.orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));

        // 알고리즘 정보 추가
        List<String> algorithmNames = new ArrayList<>();
        List<AlgorithmProblem> algorithmProblems = algorithmProblemRepository.findByProblem(problem);
        for (AlgorithmProblem algorithmProblem : algorithmProblems) {
            Algorithm algorithm = algorithmProblem.getAlgorithm();
            algorithmNames.add(algorithm.getAlgorithmName());
        }

        // Solution 정보 추가
        Optional<Solution> optionalSolution = solutionRepository.findByProblemProblemId(problemId);
        Solution solution = optionalSolution.orElseThrow(() -> new NoSuchIdException(Code.SOLUTION_NOT_FOUND, problemId));



        // 문제 정보를 DTO로 변환하여 반환
        ProblemDetailResponseDTO problemData = new ProblemDetailResponseDTO();
        problemData.setUserId(userId);
        problemData.setProblemId(problem.getProblemId());
        problemData.setProblemNumber(problem.getProblemNumber());
        problemData.setProblemTitle(problem.getProblemTitle());
        problemData.setProblemUrl(problem.getProblemUrl());
        problemData.setProblemDescription(problem.getProblemDescription());
        problemData.setProblemDifficulty(problem.getProblemDifficulty());
        problemData.setProblemPlatform(problem.getProblemPlatform());
        problemData.setProblemState(problem.getProblemState());
        problemData.setProblemMemo(problem.getProblemMemo());
        problemData.setCreatedAt(problem.getCreatedAt());
        // updatedAt 필드 값 설정
        if (problem.getUpdatedAt() != null) {
            problemData.setUpdatedAt(problem.getUpdatedAt());
        } else {
            // 문제가 생성된 후 업데이트된 적이 없으면 현재 시간으로 설정
            problemData.setUpdatedAt(new Date());
        }
        problemData.setAlgorithmId(problem.getAlgorithmId());
        problemData.setAlgorithmName(algorithmNames);


        // Solution 정보 추가
        problemData.setSolutionId(solution.getSolutionId());
        problemData.setContent(solution.getContent());
        problemData.setCode(solution.getCode());
        problemData.setCodeLanguage(solution.getCodeLanguage());
        problemData.setCodeCorrect(solution.isCodeCorrect());
        problemData.setCodeMemory(solution.getCodeMemory());
        problemData.setCodeTime(solution.getCodeTime());


        return problemData;
    }


    // problemMemo 내용 수정
    @Transactional
    public void updateProblemMemo(Long userId, Long problemId, String memo) {
        // 해당 사용자와 문제가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));

        // 문제의 소유자인지 확인
        if (!problem.getUser().equals(user)) {
            // throw new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId);
            throw new NoSuchIdException("해당 사용자는 문제를 수정할 권한이 없습니다.", Code.UNAUTHORIZED_USER, userId);

        }

        // 메모 업데이트
        problem.setProblemMemo(memo.replaceAll("\"", ""));
        problemRepository.save(problem);

    }
}