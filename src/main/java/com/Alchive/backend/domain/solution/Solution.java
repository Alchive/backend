package com.Alchive.backend.domain.solution;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.dto.request.SolutionRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "boardId", nullable = false)
    private Board board;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", length = 20, nullable = false)
    private SolutionLanguage language;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private SolutionStatus status;

    @Column(name = "memory")
    private Integer memory;

    @Column(name = "time")
    private Integer time;

    @Column(name = "submitAt")
    private Date submitAt;

    @Builder
    public Solution(Board board, String content, SolutionLanguage language, String description, SolutionStatus status, Integer memory, Integer time, Date submitAt) {
        this.board = board;
        this.content = content;
        this.language = language;
        this.description = description;
        this.status = status;
        this.memory = memory;
        this.time = time;
        this.submitAt = submitAt;
    }

    public static Solution of(Board board, SolutionRequest solutionRequest) {
        return Solution.builder()
                .board(board)
                .content(solutionRequest.getContent())
                .language(solutionRequest.getLanguage())
                .description(solutionRequest.getDescription())
                .status(solutionRequest.getStatus())
                .memory(solutionRequest.getMemory())
                .time(solutionRequest.getTime())
                .submitAt(solutionRequest.getSubmitAt())
                .build();
    }

    public Solution update(SolutionRequest solutionRequest) {
        this.content = solutionRequest.getContent();
        this.language = solutionRequest.getLanguage();
        this.description = solutionRequest.getDescription();
        this.status = solutionRequest.getStatus();
        this.memory = solutionRequest.getMemory();
        this.time = solutionRequest.getTime();
        this.submitAt = solutionRequest.getSubmitAt();
        return this;
    }

    public Solution softDelete() {
        this.isDeleted = true;
        return this;
    }

}