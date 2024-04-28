package com.Alchive.backend.config.error.exception.token;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class TokenExpiredException extends BusinessException {
    public TokenExpiredException(String tokenType, String token) {
        super(ErrorCode.TOKEN_EXPIRED, " - " + tokenType + ": " + token);
    }
}
