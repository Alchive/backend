package com.Alchive.backend.dto.request;

import com.Alchive.backend.domain.problem.ProblemDifficulty;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProblemCreateRequest {
    @NotNull(message = "문제 번호는 필수입니다. ")
    private Integer number;
    @NotNull(message = "문제 제목은 필수입니다. ")
    private String title;
    @NotNull(message = "문제 내용은 필수입니다. ")
    private String content;
    @NotNull(message = "문제 url은 필수입니다. ")
    private String url;
    @NotNull(message = "문제 난이도는 필수입니다. ")
    private ProblemDifficulty difficulty;
    @NotNull(message = "문제 플랫폼은 필수입니다. ")
    private ProblemPlatform platform;
    private List<String> algorithms;
}
