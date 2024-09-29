package com.Alchive.backend.domain.board;

import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
@Getter
@Entity
@Table(name = "board")
public class Board {
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

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "memo")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private BoardStatus status;

    @Column(name = "description")
    private String description;

    public static Board of(Problem problem, User user, BoardCreateRequest boardCreateRequest) {
        return Board.builder()
                .problem(problem)
                .user(user)
                .memo(boardCreateRequest.getMemo())
                .status(boardCreateRequest.getStatus())
                .description(boardCreateRequest.getDescription())
                .build();
    }

    public Board update(String memo) {
        this.memo = memo;
        return this;
    }

    public Board softDelete() {
        this.isDeleted = true;
        return this;
    }
}