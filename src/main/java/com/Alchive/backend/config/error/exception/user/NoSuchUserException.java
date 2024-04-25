package com.Alchive.backend.config.error.exception.user;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchUserException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String user;
}
