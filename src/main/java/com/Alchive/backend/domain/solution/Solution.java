package com.Alchive.backend.domain.solution;

import com.Alchive.backend.domain.BaseEntity;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.dto.request.SolutionCreateRequest;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE solution SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "solution")
public class Solution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", length = 20, nullable = false)
    private SolutionLanguage language;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private SolutionStatus status;

    @Column(name = "memory")
    private Integer memory;

    @Column(name = "time")
    private Integer time;

    @Column(name = "submit_at")
    private LocalDateTime submitAt;

    public static Solution of(Board board, SolutionCreateRequest solutionRequest) {
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

    public Solution update(SolutionUpdateRequest solutionRequest) {
        this.description = solutionRequest.getDescription();
        return this;
    }
}