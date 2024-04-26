package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class UnAuthorizedUserException extends BusinessException {
    public UnAuthorizedUserException(Long userId) {
        super(ErrorCode.PROBLEM_USER_UNAUTHORIZED, "userId", userId);
    }
}
