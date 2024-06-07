package com.Alchive.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProblemMemoUpdateRequest {
    @NotNull(message = "문제 아이디는 필수입니다.")
    private Long problemId;
    private String problemMemo;
}
