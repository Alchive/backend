package com.Alchive.backend.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchUserException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Long userId;
}
