package com.Alchive.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString // json 형식으로 반환하기 위함
@Setter // "
@Getter // "
public class ProblemListResponseDTO {

    private Long problemId;
    private int problemNumber;
    private String problemTitle;
    private String problemDifficulty;
    private String problemPlatform;
    private String problemState;
    private List<String> algorithmName;

}
