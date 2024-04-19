package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.SubmitProblemCreateRequest;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.repository.AlgorithmProblemRepository;
import com.Alchive.backend.repository.AlgorithmRepository;
import com.Alchive.backend.repository.ProblemRepository;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j // 로그
@RequiredArgsConstructor
@Service
@Repository
public class ProblemService {

    private final SolutionService solutionService;
    private final TokenService tokenService;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;

    // 미제출 문제 저장
    @Transactional
    public void createProblem(HttpServletRequest tokenRequest, ProblemCreateRequest problemRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        // 문제 중복 검사 - userid, problemnumber, platform으로 검사
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, problemRequest.getProblemNumber(), problemRequest.getProblemPlatform());
        if (problem != null) { // 이미 저장된 문제인 경우: 메모만 업데이트
            problem.update(problemRequest.getProblemMemo());
            problemRepository.save(problem);
        } else { // 신규 문제인 경우: 문제 저장, 알고리즘 저장
            Problem newProblem = Problem.builder()
                    .user(user)
                    .problemNumber(problemRequest.getProblemNumber())
                    .problemTitle(problemRequest.getProblemTitle())
                    .problemUrl(problemRequest.getProblemUrl())
                    .problemDescription(problemRequest.getProblemDescription())
                    .problemDifficulty(problemRequest.getProblemDifficulty())
                    .problemPlatform(problemRequest.getProblemPlatform())
                    .problemMemo(problemRequest.getProblemMemo())
                    .problemState(problemRequest.getProblemState())
                    .build();
            problemRepository.save(newProblem);
            saveAlgorihtmNames(newProblem, problemRequest.getAlgorithmNames());
        }
    }

    // 맞/틀 문제 저장
    public void createProblemSubmit(HttpServletRequest tokenRequest, SubmitProblemCreateRequest problemRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        // 문제 중복 검사 - userid, problemnumber, platform으로 검사
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, problemRequest.getProblemNumber(), problemRequest.getProblemPlatform());
        // 문제 저장 or 업데이트
        if (problem != null) {
            log.info("이미 저장된 문제");
            // 이미 저장된 문제인 경우: 메모와 정답 여부 업데이트
            problem.update(problemRequest.getProblemMemo(), problemRequest.getProblemState());
            problemRepository.save(problem);
        } else {
            log.info("신규 문제");
            // 신규 문제인 경우: 문제 저장, 알고리즘 저장
            Problem newProblem = Problem.builder()
                    .user(user)
                    .problemNumber(problemRequest.getProblemNumber())
                    .problemTitle(problemRequest.getProblemTitle())
                    .problemUrl(problemRequest.getProblemUrl())
                    .problemDescription(problemRequest.getProblemDescription())
                    .problemDifficulty(problemRequest.getProblemDifficulty())
                    .problemPlatform(problemRequest.getProblemPlatform())
                    .problemMemo(problemRequest.getProblemMemo())
                    .problemState(problemRequest.getProblemState())
                    .build();
            problem = problemRepository.save(newProblem);
            saveAlgorihtmNames(newProblem, problemRequest.getAlgorithmNames());
        }
        log.warn("problem: " + problem);
        // 풀이 저장
        solutionService.saveSolution(problem, problemRequest);
    }

    public void saveAlgorihtmNames(Problem problem, List<String> algorithmNames) { // 알고리즘 저장
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

    // 문제 저장 여부 검사
    public boolean checkProblem(HttpServletRequest tokenRequest, int problemNumber, String platform) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, problemNumber, platform);
        return (problem != null);
    }

    // 플랫폼 별 조회
    public List<ProblemListResponseDTO> getProblemsByPlatform(HttpServletRequest tokenRequest, String platform) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
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
    public List<ProblemListResponseDTO> getProblemsSearch(HttpServletRequest tokenRequest, String keyword, String category) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        if (!userRepository.existsByUserId(userId)) { // userId가 db에 존재하지 않을 경우
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
        }
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
    public List<ProblemListResponseDTO> getProblemsByUserId(HttpServletRequest tokenRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
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
    public void deleteProblem(HttpServletRequest tokenRequest, Long problemId) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        Problem problem = problemRepository.findById(problemId)
                        .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));
        if (!(problem.getUser().getUserId() == userId)) { // 해당 문제의 작성자인지 확인
            throw new NoSuchIdException(Code.PROBLEM_USER_UNAUTHORIZED, problemId);
        }
        problemRepository.delete(problem);
    }

}
