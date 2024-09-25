package com.Alchive.backend.domain.solution;

public enum SolutionStatus {
    CORRECT("정답"),
    INCORRECT("오답");

    private final String description;

    SolutionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
