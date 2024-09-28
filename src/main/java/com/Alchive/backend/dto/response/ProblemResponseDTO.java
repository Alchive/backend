package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.problem.ProblemDifficulty;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ProblemResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer number;
    private String title;
    private String content;
    private String url;
    private ProblemDifficulty difficulty;
    private ProblemPlatform platform;

    public ProblemResponseDTO(Problem problem) {
        this.id = problem.getId();
        this.createdAt = problem.getCreatedAt();
        this.updatedAt = problem.getUpdatedAt();
        this.number = problem.getNumber();
        this.title = problem.getTitle();
        this.content = problem.getContent();
        this.url = problem.getUrl();
        this.difficulty = problem.getDifficulty();
        this.platform = problem.getPlatform();
    }
}
