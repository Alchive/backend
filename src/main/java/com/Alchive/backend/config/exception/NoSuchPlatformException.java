package com.Alchive.backend.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchPlatformException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String platform;
}
