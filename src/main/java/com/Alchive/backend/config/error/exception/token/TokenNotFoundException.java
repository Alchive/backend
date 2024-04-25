package com.Alchive.backend.config.error.exception.token;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String tokenType;
}
