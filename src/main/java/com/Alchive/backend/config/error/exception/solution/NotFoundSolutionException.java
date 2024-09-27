package com.Alchive.backend.config.error.exception.solution;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class NotFoundSolutionException extends BusinessException {
    public NotFoundSolutionException() { super(ErrorCode.SOLUTION_NOT_FOUND); }
}
