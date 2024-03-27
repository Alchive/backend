package com.Alchive.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum Code implements ErrorCode{
    RESOURCE_NOT_FOUND(HttpStatusCode.valueOf(422), "유저 정보를 불러오는 데 실패했습니다. "),
    ;

    private final HttpStatusCode httpStatusCode;
    private final String message;
}