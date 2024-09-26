package com.Alchive.backend.domain.problem;

public enum ProblemDifficulty {

    LEVEL1("레벨 1"),
    LEVEL2("레벨 2"),
    LEVEL3("레벨 3"),
    LEVEL4("레벨 4"),
    LEVEL5("레벨 5");

    private final String description;

    ProblemDifficulty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
