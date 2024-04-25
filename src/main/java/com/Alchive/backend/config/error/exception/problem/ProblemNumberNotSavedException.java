package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProblemNumberNotSavedException extends RuntimeException{
    private final ErrorCode errorCode;
    private final int problemNumber;
    private final String platform;
}
