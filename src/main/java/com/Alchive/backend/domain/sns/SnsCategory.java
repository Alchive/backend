package com.Alchive.backend.domain.sns;

public enum SnsCategory {
    SLACK("슬랙"),
    DISCORD("디스코드");

    private final String description;

    SnsCategory(String description) { this.description = description; }

    public String getDescription() { return description; }
}
