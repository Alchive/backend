package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.*;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.SubmitProblemCreateRequest;
import com.Alchive.backend.dto.response.ProblemDetailResponseDTO;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {
    private final TokenService tokenService;
    private final SolutionService solutionService;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final AlgorithmRepository algorithmRepository;
    private final AlgorithmProblemRepository algorithmProblemRepository;

    // 미제출 문제 저장
    @Transactional
    public void createProblem(Long userId, ProblemCreateRequest request) {
        // user 무결성 확인 - 임시
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        // 문제 중복 검사 - userid, problemnumber, platform으로 검사
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, request.getProblemNumber(), request.getProblemPlatform());
        if (problem != null) {
            log.info("이미 저장된 문제");
            // 이미 저장된 문제인 경우: 메모만 업데이트
            problem.update(request.getProblemMemo());
            problemRepository.save(problem);
        } else {
            log.info("신규 문제");
            // 신규 문제인 경우: 문제 저장, 알고리즘 저장
            Problem newProblem = Problem.builder()
                    .user(user)
                    .problemNumber(request.getProblemNumber())
                    .problemTitle(request.getProblemTitle())
                    .problemUrl(request.getProblemUrl())
                    .problemDescription(request.getProblemDescription())
                    .problemDifficulty(request.getProblemDifficulty())
                    .problemPlatform(request.getProblemPlatform())
                    .problemMemo(request.getProblemMemo())
                    .problemState(request.getProblemState())
                    .build();
            problemRepository.save(newProblem);
            saveAlgorihtmNames(newProblem, request.getAlgorithmNames());
        }
    }

    // 맞/틀 문제 저장
    public void createProblemSubmit(Long userId, SubmitProblemCreateRequest request) {
        // user 무결성 확인 - 임시
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        // 문제 중복 검사 - userid, problemnumber, platform으로 검사
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, request.getProblemNumber(), request.getProblemPlatform());
        // 문제 저장 or 업데이트
        if (problem != null) {
            log.info("이미 저장된 문제");
            // 이미 저장된 문제인 경우: 메모와 정답 여부 업데이트
            problem.update(request.getProblemMemo(), request.getProblemState());
            problemRepository.save(problem);
        } else {
            log.info("신규 문제");
            // 신규 문제인 경우: 문제 저장, 알고리즘 저장
            Problem newProblem = Problem.builder()
                    .user(user)
                    .problemNumber(request.getProblemNumber())
                    .problemTitle(request.getProblemTitle())
                    .problemUrl(request.getProblemUrl())
                    .problemDescription(request.getProblemDescription())
                    .problemDifficulty(request.getProblemDifficulty())
                    .problemPlatform(request.getProblemPlatform())
                    .problemMemo(request.getProblemMemo())
                    .problemState(request.getProblemState())
                    .build();
            problem = problemRepository.save(newProblem);
            saveAlgorihtmNames(newProblem, request.getAlgorithmNames());
        }
        log.warn("problem: " + problem);
        // 풀이 저장
        solutionService.saveSolution(problem, request);
    }

    public void saveAlgorihtmNames(Problem problem, List<String> algorithmNames) { // 알고리즘 저장
        log.info("알고리즘 저장 함수 호출");
        for (String algorithmName : algorithmNames) {
            // 알고리즘 존재 여부 확인
            if (!algorithmRepository.existsByAlgorithmName(algorithmName)) {
                // 존재하지 않는 알고리즘인 경우: 저장
                Algorithm algorithm = com.Alchive.backend.domain.Algorithm.builder()
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
    public boolean checkProblem(Long userId, int problemNumber, String platform) {
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, problemNumber, platform);
        return (problem != null);
    }

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
        return addProblemList(problems);
    }

    // 문제 검색
    public List<ProblemListResponseDTO> getProblemsSearch(Long userId, String keyword, String category) {
        // userId가 db에 존재하지 않을 경우
        if (!userRepository.existsByUserId(userId)) {
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

        return addProblemList(problems);
    }

    // 사용자가 작성한 전체 목록 조회
    public List<ProblemListResponseDTO> getProblemsByUserId(Long userId) {
        List<Problem> userProblems = problemRepository.findByUserUserId(userId);
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchIdException(Code.USER_NOT_FOUND, userId);
        }
        return addProblemList(userProblems);
    }

    // 문제 목록 반환하기 - 전체 목록 조회 & 플랫폼 별 조회 & 문제 검색
    public List<ProblemListResponseDTO> addProblemList(List<Problem> problems) {
        List<ProblemListResponseDTO> problemDataList = new ArrayList<>();
        for (Problem problem : problems) {
            List<Algorithm> algorithmList = addAlgorithmList(problem); // 알고리즘 배열 추가
            ProblemListResponseDTO problemData = ProblemListResponseDTO.builder()
                    .problemId(problem.getProblemId())
                    .problemNumber(problem.getProblemNumber())
                    .problemTitle(problem.getProblemTitle())
                    .problemDifficulty(problem.getProblemDifficulty())
                    .problemPlatform(problem.getProblemPlatform())
                    .problemState(problem.getProblemState())
                    .algorithmList(algorithmList)
                    .build();
            problemDataList.add(problemData);
        }
        return problemDataList;
    }

    // 문제 삭제
    @Transactional
    public void deleteProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));
        problemRepository.delete(problem);
    }

    // 단일 문제 조회
    public ProblemDetailResponseDTO getProblemByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));
        // Algorithm 정보
        List<Algorithm> algorithmList = addAlgorithmList(problem);
        // Solution 정보
        List<ProblemDetailResponseDTO.SolutionInfo> solutionInfos = new ArrayList<>();
        List<Solution> solutions = solutionRepository.findAllByProblemProblemId(problemId);
        for (Solution solution : solutions) {
            ProblemDetailResponseDTO.SolutionInfo solutionInfo = ProblemDetailResponseDTO.SolutionInfo.builder()
                    .solutionId(solution.getSolutionId())
                    .content(solution.getContent())
                    .code(solution.getCode())
                    .codeLanguage(solution.getCodeLanguage())
                    .codeCorrect(solution.isCodeCorrect())
                    .codeMemory(solution.getCodeMemory())
                    .codeTime(solution.getCodeTime())
                    .build();
            solutionInfos.add(solutionInfo);
        }
        ProblemDetailResponseDTO problemData = ProblemDetailResponseDTO.builder()
                .problemId(problem.getProblemId())
                .userId(problem.getUser().getUserId())
                .problemNumber(problem.getProblemNumber())
                .problemTitle(problem.getProblemTitle())
                .problemUrl(problem.getProblemUrl())
                .problemDescription(problem.getProblemDescription())
                .problemDifficulty(problem.getProblemDifficulty())
                .problemPlatform(problem.getProblemPlatform())
                .problemMemo(problem.getProblemMemo())
                .problemState(problem.getProblemState())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .algorithmList(algorithmList)
                .solutionList(solutionInfos)
                .build();
        return problemData;
    }

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

    @Transactional
    public void updateProblemMemo(HttpServletRequest tokenRequest, Long problemId, String problemMemo) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchIdException(Code.PROBLEM_NOT_FOUND, problemId));
        if (problem.getUser().getUserId() != userId) { // 작성자 검사
            throw new NoSuchIdException(Code.PROBLEM_USER_UNAUTHORIZED, problemId);
        }
        problem.update(problemMemo);
    }
}