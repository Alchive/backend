package com.Alchive.backend.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),"잘못된 요청"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(),"인증 필요"),
    _FORBIDDEN(HttpStatus.FORBIDDEN.value(), "금지된 요청"),

    // USER
    USER_EMAIL_EXISTS(HttpStatus.CONFLICT.value(), "유저 이메일 중복, userEmail: "),
    USER_NAME_EXISTS(HttpStatus.CONFLICT.value(), "유저 이름 중복, userName: "),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저 아이디, userId: "),
    USER_CREATED(HttpStatus.CREATED.value(), "유저가 생성되었습니다. [id: "), // todo: result로 이동하기

    // PROBLEM
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 문제, problemId: "),
    PROBLEM_CREATED(HttpStatus.CREATED.value(), "문제가 생성되었습니다. [problem: "), // todo: result로 이동하기
    PROBLEM_USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "승인되지 않은 접근"),
    PLATFORM_INVALID(HttpStatus.NOT_FOUND.value(), "플랫폼 미지원, platform: "),
    CATEGORY_INVALID(HttpStatus.NOT_FOUND.value(), "카테고리 미지원, category: "),
    PROBLEM_NOT_SAVED(HttpStatus.NOT_FOUND.value(), "저장되지 않은 문제, problemNumber, platform: "),

    // SOLUTION
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 풀이, solutionId: "),

    // AUTH
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "액세스 토큰 만료, token: "),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰 만료, token: "),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "토큰을 찾을 수 없음, tokenType: "),
    ;

    private final int httpStatus;
    private final String message;
}