package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.solution.Solution;
import com.Alchive.backend.domain.solution.SolutionLanguage;
import com.Alchive.backend.domain.solution.SolutionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
public class SolutionResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Board board;
    private String content;
    private SolutionLanguage language;
    private String description;
    private SolutionStatus status;
    private Integer memory;
    private Integer time;
    private LocalDateTime submitAt;

    public SolutionResponseDTO(Solution solution) {
        this.id = solution.getId();
        this.createdAt = solution.getCreatedAt();
        this.updatedAt = solution.getUpdatedAt();
        this.board = solution.getBoard();
        this.content = solution.getContent();
        this.language = solution.getLanguage();
        this.description = solution.getDescription();
        this.status = solution.getStatus();
        this.memory = solution.getMemory();
        this.time = solution.getTime();
        this.submitAt = solution.getSubmitAt();
    }
}
