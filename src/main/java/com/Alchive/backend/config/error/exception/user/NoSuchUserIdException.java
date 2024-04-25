package com.Alchive.backend.config.error.exception.user;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchUserIdException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Long userId;

    public NoSuchUserIdException(String message, ErrorCode errorCode, Long userId) {
        super(message);
        this.errorCode = errorCode;
        this.userId = userId;
    }
}
