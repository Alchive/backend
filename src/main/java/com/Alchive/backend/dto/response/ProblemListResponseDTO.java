package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.Algorithm;
import com.Alchive.backend.domain.Problem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProblemListResponseDTO {

    private Long problemId;
    private int problemNumber;
    private String problemTitle;
    private String problemDifficulty;
    private String problemPlatform;
    private String problemState;
    private List<Algorithm> algorithmList;

    public static ProblemListResponseDTO of(Problem problem, List<Algorithm> algorithmList) {
        return ProblemListResponseDTO.builder()
                .problemId(problem.getProblemId())
                .problemNumber(problem.getProblemNumber())
                .problemTitle(problem.getProblemTitle())
                .problemDifficulty(problem.getProblemDifficulty())
                .problemPlatform(problem.getProblemPlatform())
                .problemState(problem.getProblemState())
                .algorithmList(algorithmList)
                .build();
    }
}
