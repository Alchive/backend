package com.Alchive.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProblemDetailResponseDTO {

    private Long problemId;
    private Long userId;
    private int problemNumber;
    private String problemTitle;
    private String problemUrl;
    private String problemDescription;
    private String problemDifficulty;
    private String problemPlatform;
    private String problemMemo;
    private String problemState;
    private Date createdAt;
    private Date updatedAt;
    private Long algorithmId;
    private List<String> algorithmName;

    public void setAlgorithmName(List<String> algorithmName) {
        this.algorithmName = algorithmName;
    }
}
