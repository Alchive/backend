package com.Alchive.backend.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),"B001", "서버 에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST.value(),"B002","잘못된 요청"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(),"B003","인증 필요"),
    _FORBIDDEN(HttpStatus.FORBIDDEN.value(),"B004","금지된 요청"),

    // USER
    USER_EMAIL_EXISTS(HttpStatus.CONFLICT.value(), "U001","유저 이메일 중복"),
    USER_NAME_EXISTS(HttpStatus.CONFLICT.value(), "U002","유저 이름 중복"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "U003","존재하지 않는 유저 아이디"),
//    USER_CREATED(HttpStatus.CREATED.value(), "U004","유저가 생성되었습니다. [id: "), // todo: result로 이동하기

    // PROBLEM
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "P001","존재하지 않는 문제"),
//    PROBLEM_CREATED(HttpStatus.CREATED.value(), "P002","문제가 생성되었습니다."), // todo: result로 이동하기
    PROBLEM_USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "P003","승인되지 않은 접근"),
    PLATFORM_INVALID(HttpStatus.NOT_FOUND.value(), "P004","플랫폼 미지원"),
    CATEGORY_INVALID(HttpStatus.NOT_FOUND.value(), "P005","카테고리 미지원"),
    PROBLEM_NOT_SAVED(HttpStatus.NOT_FOUND.value(), "P006","저장되지 않은 문제"),

    // SOLUTION
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "S001","존재하지 않는 풀이"),

    // AUTH
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "A001","토큰 만료"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "A002","토큰을 찾을 수 없음"),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;
}