package com.Alchive.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code implements ErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),"잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(),"인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN.value(), "금지된 요청입니다."),

    // USER
    USER_EMAIL_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 유저 이메일입니다. [email: "),
    USER_NAME_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 유저 이름입니다. [name: "),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "유저 정보를 불러오는 데 실패했습니다. [id: "),
    USER_CREATED(HttpStatus.CREATED.value(), "유저가 생성되었습니다. [id: "),

    // PROBLEM
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "문제를 불러오는 데 실패했습니다. [problem: "),
    PROBLEM_CREATED(HttpStatus.CREATED.value(), "문제가 생성되었습니다. [problem: "),
    PLATFORM_INVALID(HttpStatus.NOT_FOUND.value(), "지원하지 않는 플랫폼입니다. [platform: "),

    // SEARCH
    CATEGORY_INVALID(HttpStatus.NOT_FOUND.value(), "지원하지 않는 카테고리입니다. [category: "),
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "키워드에 해당하는 문제가 없습니다. [keyword: "),

    // SOLUTION
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "풀이를 불러오는 데 실패했습니다. [solution: ");
    ;

    private final int httpStatus;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus;
    }
}