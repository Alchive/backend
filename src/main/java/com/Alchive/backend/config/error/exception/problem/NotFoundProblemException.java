package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class NotFoundProblemException extends BusinessException {
    public NotFoundProblemException() { super(ErrorCode.PROBLEM_NOT_FOUND); }
}
