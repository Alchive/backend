package com.Alchive.backend.config.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "G001", "서버 에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "G002", "잘못된 요청"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "G003", "인증 필요"),
    _FORBIDDEN(HttpStatus.FORBIDDEN.value(), "G004", "금지된 요청"),

    // AUTH
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "A001", "토큰 만료"),
    TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED.value(), "A002", "토큰을 찾을 수 없음"),
    USERID_NOT_MATCHED(HttpStatus.UNAUTHORIZED.value(), "A003", "작성자 ID와 요청자 ID가 일치하지 않음"),

    // USER
    USER_EMAIL_EXISTS(HttpStatus.CONFLICT.value(), "U001", "유저 이메일 중복"),
    USER_NAME_EXISTS(HttpStatus.CONFLICT.value(), "U002", "유저 이름 중복"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "U003", "존재하지 않는 유저 아이디"),

    // BOARD
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "B001", "게시물이 존재하지 않음"),

    // SOLUTION
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "S001", "풀이가 존재하지 않음"),

    // PROBLEM
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "P001", "문제가 존재하지 않음");

    private final int httpStatus;
    private final String code;
    private final String message;
}