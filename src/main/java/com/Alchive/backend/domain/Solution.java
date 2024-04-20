package com.Alchive.backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solutionId", columnDefinition = "INT")
    private Long solutionId;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

    @Column(name = "content")
    private String content;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "codeLanguage")
    private String codeLanguage;

    @Column(name = "codeCorrect", nullable = false)
    private boolean codeCorrect;

    @Column(name = "codeMemory")
    private int codeMemory;

    @Column(name = "codeTime")
    private int codeTime;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Builder
    public Solution(Long solutionId, Problem problem, String content, String code, String codeLanguage, boolean codeCorrect, int codeMemory, int codeTime, Date createdAt, Date updatedAt) {
        this.solutionId = solutionId;
        this.problem = problem;
        this.content = content;
        this.code = code;
        this.codeLanguage = codeLanguage;
        this.codeCorrect = codeCorrect;
        this.codeMemory = codeMemory;
        this.codeTime = codeTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Solution update(String content) {
        this.content = content;
        this.updatedAt = new Date();

        return this;
    }
}