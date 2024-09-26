package com.Alchive.backend.domain.solution;

import com.Alchive.backend.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private Boolean isDeleted;

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

}