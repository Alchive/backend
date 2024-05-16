package com.Alchive.backend.domain;

import com.Alchive.backend.dto.request.ProblemCreateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor // 기본 생성자
@Getter
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

    @Builder
    public Problem(User user, int problemNumber, String problemTitle, String problemUrl, String problemDescription, String problemDifficulty, String problemPlatform, String problemMemo, String problemState) {
        this.user = user;
        this.problemNumber = problemNumber;
        this.problemTitle = problemTitle;
        this.problemUrl = problemUrl;
        this.problemDescription = problemDescription;
        this.problemDifficulty = problemDifficulty;
        this.problemPlatform = problemPlatform;
        this.problemMemo = problemMemo;
        this.problemState = problemState;
        this.createdAt = new Date();
    }

    public static Problem of(User user, ProblemCreateRequest problemRequest) {
        return Problem.builder()
                .user(user)
                .problemNumber(problemRequest.getProblemNumber())
                .problemTitle(problemRequest.getProblemTitle())
                .problemUrl(problemRequest.getProblemUrl())
                .problemDescription(problemRequest.getProblemDescription())
                .problemDifficulty(problemRequest.getProblemDifficulty())
                .problemPlatform(problemRequest.getProblemPlatform())
                .problemMemo(problemRequest.getProblemMemo())
                .problemState(problemRequest.getProblemState())
                .build();
    }

    public Problem update(String problemMemo) { // 메모 update 시 사용
        this.problemMemo = problemMemo;
        this.updatedAt = new Date();
        return this;
    }

    public Problem update(String problemMemo,String problemState) { // 동일한 문제 재제출(저장) 시 사용
        this.problemMemo = problemMemo;
        this.problemState = problemState;
        this.updatedAt = new Date();
        return this;
    }
}