package com.Alchive.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProblemCreateRequest {
    @NotNull
    int problemNumber;
    @NotNull
    String problemTitle;
    @NotNull
    String problemUrl;
    @NotNull
    String problemDescription;
    @NotNull
    String problemDifficulty;
    @NotNull
    String problemPlatform;
    List<String> algorithmNames;
    String problemMemo;
    @NotNull
    String problemState;
}
