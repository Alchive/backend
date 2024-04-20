package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.Algorithm;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProblemListResponseDTO {

    private Long problemId;
    private int problemNumber;
    private String problemTitle;
    private String problemDifficulty;
    private String problemPlatform;
    private String problemState;
    private List<Algorithm> algorithmList;

}
