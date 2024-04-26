package com.Alchive.backend.config.error.exception.solution;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoSuchSolutionIdException extends BusinessException {
    public NoSuchSolutionIdException(Long solutionId) {
        super(ErrorCode.SOLUTION_NOT_FOUND, "solutionId", solutionId);
    }
}
