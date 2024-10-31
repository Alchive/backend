package com.Alchive.backend.domain.solution;

public enum SolutionLanguage {
    PYTHON("python"),
    CPP("c++"),
    C("c"),
    JAVASCRIPT("javascript"),
    JAVA("java"),
    CSHARP("c#"),
    KOTLIN("kotlin"),
    RUBY("ruby"),
    GO("go"),
    SCALA("scala"),
    SWIFT("swift");

    private final String description;

    SolutionLanguage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
