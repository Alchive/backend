package com.Alchive.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SubmitProblemCreateRequest {
    // Problem
    @NotNull(message = "문제 번호는 필수입니다.")
    int problemNumber;
    @NotBlank(message = "문제 제목은 필수입니다.")
    String problemTitle;
    @NotBlank(message = "문제 Url은 필수입니다.")
    String problemUrl;
    @NotBlank(message = "문제 설명은 필수입니다.")
    String problemDescription;
    @NotBlank(message = "문제 난이도는 필수입니다.")
    String problemDifficulty;
    @NotBlank(message = "문제 플랫폼은 필수입니다.")
    String problemPlatform;
    List<String> algorithmNames;
    String problemMemo;
    @NotBlank(message = "문제 상태는 필수입니다.")
    String problemState;

    // Solution
    String content;
    @NotBlank(message = "코드 내용은 필수입니다.")
    String code;
    @NotBlank(message = "코드 언어는 필수입니다.")
    String codeLanguage;
    @NotNull(message = "정답 여부는 필수입니다.")
    boolean codeCorrect;
    int codeMemory;
    int codeTime;
}
