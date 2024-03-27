package com.Alchive.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneralException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Long userId;
}
