package com.Alchive.backend.domain.board;

public enum BoardStatus {
    CORRECT("정답"),
    INCORRECT("오답"),
    NOT_SUBMITTED("미제출"),
    COMPLETED("완료");

    private final String description;

    BoardStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
