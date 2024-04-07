package com.Alchive.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolutionUpdateRequest {
    private Long solutionId;
    private String content;
}
