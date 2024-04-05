package com.Alchive.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeUpdateRequest {
    private Long codeId;
    private String solution;
}
