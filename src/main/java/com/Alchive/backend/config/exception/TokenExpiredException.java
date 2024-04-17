package com.Alchive.backend.config.exception;

import com.Alchive.backend.config.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@RequiredArgsConstructor
public class TokenExpiredException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String token;
}
