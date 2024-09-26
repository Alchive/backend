package com.Alchive.backend.config.error.exception.user;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoSuchUserIdException extends BusinessException {

    public NoSuchUserIdException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
