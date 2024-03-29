package com.Alchive.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code implements ErrorCode{
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(),"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN.value(), "금지된 요청입니다."),

    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저 정보를 불러오는 데 실패했습니다."),
    USER_CREATED(HttpStatus.CREATED.value(), "유저가 생성되었습니다."),

    // PROBLEM
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "문제를 불러오는 데 실패했습니다."),
    PROBLEM_CREATED(HttpStatus.CREATED.value(), "문제가 생성되었습니다."),
    PLATFORM_INVALID(HttpStatus.NOT_FOUND.value(), "지원하지 않는 플랫폼입니다."),
    ;

    private final int httpStatus;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus;
    }
}