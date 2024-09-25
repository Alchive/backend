package com.Alchive.backend.domain.solution;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "isDeleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column(name = "boardId", nullable = false)
    private int boardId;

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