package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
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

    public Solution update(String content) {
        this.content = content;
        this.updatedAt = new Date();

        return this;
    }
}