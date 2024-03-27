package com.Alchive.backend.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    private UserDetailData data;

    public ApiResponse(int code, String message, UserDetailData response) {
        this.code = code;
        this.message = message;
        this.data = response;
    }
}
