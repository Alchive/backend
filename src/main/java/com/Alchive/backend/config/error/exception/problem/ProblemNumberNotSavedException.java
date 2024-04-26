package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class ProblemNumberNotSavedException extends BusinessException {
    public ProblemNumberNotSavedException(int problemNumber, String platform) {
        super(ErrorCode.PROBLEM_NOT_SAVED, "problemNumber", problemNumber, "platform", platform);
    }
}
