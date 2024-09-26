package com.Alchive.backend.domain.problem;

public enum ProblemPlatform {
    BAEKJOON("백준"),
    PROGRAMMERS("프로그래머스"),
    LEETCODE("leetcode");

    private final String description;

    ProblemPlatform(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}