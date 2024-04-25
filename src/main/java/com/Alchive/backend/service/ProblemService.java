package com.Alchive.backend.service;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.problem.NoSuchProblemIdException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.problem.NoSuchPlatformException;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final TokenService tokenService;
    private final SolutionService solutionService;
    private final AlgorithmSerivce algorithmSerivce;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final SolutionRepository solutionRepository;

    // 미제출 문제 저장
    @Transactional
    public void createProblem(HttpServletRequest tokenRequest, ProblemCreateRequest problemRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserIdException(ErrorCode.USER_NOT_FOUND, userId));
        // 문제 중복 검사 - userid, problemnumber, platform으로 검사
        Problem problem = problemRepository.findByUserUserIdAndProblemNumberAndProblemPlatform(userId, problemRequest.getProblemNumber(), problemRequest.getProblemPlatform());
        if (problem != null) { // 이미 저장된 문제인 경우: 메모만 업데이트
            problem.update(problemRequest.getProblemMemo());
            problemRepository.save(problem);
        } else { // 신규 문제인 경우: 문제 저장, 알고리즘 저장
            Problem newProblem = Problem.of(user, problemRequest);
            problemRepository.save(newProblem);
            algorithmSerivce.saveAlgorihtmNames(newProblem, problemRequest.getAlgorithmNames());
        }
    }

    // 맞/틀 문제 저장
    public void createProblemSubmit(HttpServletRequest tokenRequest, ProblemCreateRequest problemRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserIdException(ErrorCode.USER_NOT_FOUND, userId));
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
            Problem newProblem = Problem.of(user, problemRequest);
            problem = problemRepository.save(newProblem);
            algorithmSerivce.saveAlgorihtmNames(newProblem, problemRequest.getAlgorithmNames());
        }
        log.warn("problem: " + problem);
        // 풀이 저장
        solutionService.saveSolution(problem, problemRequest.getSolutionInfo());
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
        // Baekjoon, Programmers, Leetcode가 아닌 경우
        if (!platform.equals("Baekjoon") && !platform.equals("Programmers") && !platform.equals("Leetcode")) {
            throw new NoSuchPlatformException(ErrorCode.PLATFORM_INVALID, platform);
        }
        // problem 테이블에서 플랫폼으로 문제 조회
        List<Problem> problems = problemRepository.findByUserUserIdAndProblemPlatform(userId, platform);
        // 문제 리스트를 보내서 알고리즘 이름 추가하기
        return addProblemList(problems);
    }

    // 문제 검색
    public List<ProblemListResponseDTO> getProblemsSearch(HttpServletRequest tokenRequest, String keyword, String category) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        List<Problem> problems;
        if (category == null) {
            problems = problemRepository.findByUserIdAndProblemNumberOrTitleContaining(userId, keyword);
        } else if ("number".equals(category)) {
            problems = problemRepository.findByUserIdAndProblemNumberContaining(userId, keyword);
        } else if ("title".equals(category)) {
            problems = problemRepository.findByUserIdAndProblemTitleContaining(userId, keyword);
        } else {
            throw new NoSuchPlatformException(ErrorCode.CATEGORY_INVALID, category);
        }
        return addProblemList(problems);
    }

    // 사용자가 작성한 전체 목록 조회
    public List<ProblemListResponseDTO> getProblemsByUserId(HttpServletRequest tokenRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        List<Problem> userProblems = problemRepository.findByUserUserId(userId);
        if (!userRepository.existsByUserId(userId)) {
            throw new NoSuchUserIdException(ErrorCode.USER_NOT_FOUND, userId);
        }
        return addProblemList(userProblems);
    }

    // 문제 목록 반환하기 - 전체 목록 조회 & 플랫폼 별 조회 & 문제 검색
    public List<ProblemListResponseDTO> addProblemList(List<Problem> problems) {
        List<ProblemListResponseDTO> problemDataList = new ArrayList<>();
        for (Problem problem : problems) {
            List<Algorithm> algorithmList = algorithmSerivce.addAlgorithmList(problem); // 알고리즘 배열 추가
            ProblemListResponseDTO problemData = ProblemListResponseDTO.of(problem, algorithmList);
            problemDataList.add(problemData);
        }
        return problemDataList;
    }

    // 문제 삭제
    @Transactional
    public void deleteProblem(HttpServletRequest tokenRequest, Long problemId) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        Problem problem = problemRepository.findById(problemId)
                        .orElseThrow(() -> new NoSuchProblemIdException(ErrorCode.PROBLEM_NOT_FOUND, problemId));
        if ((problem.getUser().getUserId() != userId)) { // 해당 문제의 작성자인지 확인
            throw new NoSuchProblemIdException(ErrorCode.PROBLEM_USER_UNAUTHORIZED, problemId);
        }
        problemRepository.delete(problem);
    }

    // 단일 문제 조회
    public ProblemDetailResponseDTO getProblemByProblemId(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NoSuchUserIdException(ErrorCode.PROBLEM_NOT_FOUND, problemId));
        // Algorithm 정보
        List<Algorithm> algorithmList = algorithmSerivce.addAlgorithmList(problem);
        // Solution 정보
        List<ProblemDetailResponseDTO.SolutionInfo> solutionInfos = new ArrayList<>();
        List<Solution> solutions = solutionRepository.findAllByProblemProblemId(problemId);
        for (Solution solution : solutions) {
            ProblemDetailResponseDTO.SolutionInfo solutionInfo = ProblemDetailResponseDTO.SolutionInfo.of(solution);
            solutionInfos.add(solutionInfo);
        }
        return ProblemDetailResponseDTO.of(problem, algorithmList, solutionInfos);
    }

    @Transactional
    public void updateProblemMemo(HttpServletRequest tokenRequest, ProblemMemoUpdateRequest memoRequest) {
        tokenService.validateAccessToken(tokenService.resolveAccessToken(tokenRequest)); // 만료 검사
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        Problem problem = problemRepository.findById(memoRequest.getProblemId())
                .orElseThrow(() -> new NoSuchProblemIdException(ErrorCode.PROBLEM_NOT_FOUND, memoRequest.getProblemId()));
        if (!Objects.equals(problem.getUser().getUserId(), userId)) { // 작성자 검사
            throw new NoSuchProblemIdException(ErrorCode.PROBLEM_USER_UNAUTHORIZED, problem.getProblemId());
        }
        problem.update(memoRequest.getProblemMemo());
    }
}