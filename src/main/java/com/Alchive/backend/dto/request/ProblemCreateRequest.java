package com.Alchive.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProblemCreateRequest {
    @NotNull(message = "문제 번호는 필수입니다.")
    private int problemNumber;
    @NotBlank(message = "문제 제목은 필수입니다.")
    private String problemTitle;
    @NotBlank(message = "문제 Url은 필수입니다.")
    private String problemUrl;
    @NotBlank(message = "문제 설명은 필수입니다.")
    private String problemDescription;
    @NotBlank(message = "문제 난이도는 필수입니다.")
    private String problemDifficulty;
    @NotBlank(message = "문제 플랫폼은 필수입니다.")
    private String problemPlatform;
    private List<String> algorithmNames;
    private String problemMemo;
    @NotBlank(message = "문제 상태는 필수입니다.")
    private String problemState;
    private SolutionInfo solutionInfo;

    @Builder
    @Getter
    public static class SolutionInfo {
        // 풀이가 있을 시 - 제출 문제 저장 시
        private String content;
        @NotBlank(message = "코드 내용은 필수입니다.")
        private String code;
        @NotBlank(message = "코드 언어는 필수입니다.")
        private String codeLanguage;
        @NotNull(message = "정답 여부는 필수입니다.")
        boolean codeCorrect;
        String codeMemory;
        String codeTime;
    }
}
