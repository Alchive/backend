package com.Alchive.backend.config.error.exception.sns;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class InvalidGrantException extends BusinessException {
    public InvalidGrantException() { super(ErrorCode.INVALID_GRANT); }
}
