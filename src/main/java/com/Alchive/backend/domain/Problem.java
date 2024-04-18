package com.Alchive.backend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "problem")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problemId", columnDefinition = "INT")
    private Long problemId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "problemNumber", nullable = false)
    private int problemNumber;

    @Column(name = "problemTitle", nullable = false)
    private String problemTitle;

    @Column(name = "problemUrl", nullable = false)
    private String problemUrl;

    @Column(name = "problemDescription", nullable = false)
    private String problemDescription;

    @Column(name = "problemDifficulty", nullable = false)
    private String problemDifficulty;

    @Column(name = "problemPlatform", nullable = false)
    private String problemPlatform;

    @Column(name = "problemMemo")
    private String problemMemo;

    @Column(name = "problemState", nullable = false)
    private String problemState;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "algorithmId")
    private Long algorithmId;

    public Long getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(Long algorithmId) {
        this.algorithmId = algorithmId;
    }
}