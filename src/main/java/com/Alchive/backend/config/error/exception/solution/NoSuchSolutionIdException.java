package com.Alchive.backend.config.error.exception.solution;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchSolutionIdException extends RuntimeException{
    private final ErrorCode errorCode;
    private final Long solutionId;
}
