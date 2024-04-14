package com.Alchive.backend.config.exception;

import com.Alchive.backend.config.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String tokenType;
}
