package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoSuchProblemIdException extends BusinessException {
    public NoSuchProblemIdException(Long problemId) {
        super(ErrorCode.PROBLEM_NOT_FOUND, " - problemId: " + problemId);
    }
}
