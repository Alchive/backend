package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codeId", columnDefinition = "INT")
    private Long codeId;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false)
    private Problem problem;

    @Column(name = "solution")
    private String solution;

    @Column(name = "codeLanguage")
    private String codeLanguage;

    @Column(name = "codeContent", nullable = false)
    private String codeContent;

    @Column(name = "codeCorrect", nullable = false)
    private boolean codeCorrect;

    @Column(name = "codeMemory")
    private int codeMemory;

    @Column(name = "codeTime")
    private int codeTime;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

}