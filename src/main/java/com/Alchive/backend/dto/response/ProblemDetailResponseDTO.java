package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.Algorithm;
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
        private int codeMemory;
        private int codeTime;
        private Date createdAt;
        private Date updatedAt;
    }
}
