package com.Alchive.backend.dto.request;

import com.Alchive.backend.domain.problem.ProblemPlatform;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProblemNumberRequest {
    @NotNull(message = "플랫폼 정보는 필수입니다. ")
    private ProblemPlatform platform;
    @NotNull(message = "문제 번호는 필수입니다. ")
    private Integer problemNumber;
}
