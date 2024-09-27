package com.Alchive.backend.dto.request;

import com.Alchive.backend.domain.solution.SolutionLanguage;
import com.Alchive.backend.domain.solution.SolutionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class SolutionRequest {
    @NotBlank(message = "코드 내용은 필수입니다.")
    private String content;
    @NotNull(message = "언어 정보는 필수입니다.")
    private SolutionLanguage language;
    private String description;
    @NotNull(message = "정답 상태는 필수입니다.")
    private SolutionStatus status;
    @NotNull(message = "메모리 정보는 필수입니다.")
    private Integer memory;
    @NotNull(message = "실행 시간 정보는 필수입니다.")
    private Integer time;
    @NotNull(message = "제출일 정보는 필수입니다.")
    private Date submitAt;
}
