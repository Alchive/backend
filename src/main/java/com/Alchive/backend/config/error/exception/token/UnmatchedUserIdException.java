package com.Alchive.backend.config.error.exception.token;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class UnmatchedUserIdException extends BusinessException {
    public UnmatchedUserIdException() { super(ErrorCode.USERID_NOT_MATCHED); }
}
