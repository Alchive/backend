package com.Alchive.backend.config.error.exception.token;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class TokenNotExistsException extends BusinessException {
    public TokenNotExistsException(String tokenType) {
        super(ErrorCode.TOKEN_NOT_EXISTS, " - tokenType: " + tokenType);
    }
}
