package com.Alchive.backend.config.error.exception.sns;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class NoSuchSnsIdException extends BusinessException {
    public NoSuchSnsIdException() { super(ErrorCode.SNS_NOT_FOUND); }
}
