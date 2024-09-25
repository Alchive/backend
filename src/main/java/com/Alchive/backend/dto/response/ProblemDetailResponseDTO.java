package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.algorithm.Algorithm;
import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.solution.Solution;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
public class ProblemDetailResponseDTO {
    // 문제 정보
    private Long problemId;
    private Long userId;
    private int problemNumber;
    private String problemTitle;
    private String problemUrl;
    private String problemDescription;
    private String problemDifficulty;
    private String problemPlatform;
    private String problemMemo;
    private String problemState;
    private Date createdAt;
    private Date updatedAt;
    private List<Algorithm> algorithmList;
    private List<SolutionInfo> solutionList;

    @Builder
    @Getter
    public static class SolutionInfo {
        // 풀이 정보
        private Long solutionId;
        private String content;
        private String code;
        private String codeLanguage;
        private boolean codeCorrect;
        private String codeMemory;
        private String codeTime;
        private Date createdAt;
        private Date updatedAt;

        public static SolutionInfo of(Solution solution) {
            return SolutionInfo.builder()
                    .solutionId(solution.getSolutionId())
                    .content(solution.getContent())
                    .code(solution.getCode())
                    .codeLanguage(solution.getCodeLanguage())
                    .codeCorrect(solution.isCodeCorrect())
                    .codeMemory(solution.getCodeMemory())
                    .codeTime(solution.getCodeTime())
                    .build();
        }
    }

    public static ProblemDetailResponseDTO of(Problem problem, List<Algorithm> algorithmList, List<SolutionInfo> solutionInfos) {
        return ProblemDetailResponseDTO.builder()
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
    }
}
