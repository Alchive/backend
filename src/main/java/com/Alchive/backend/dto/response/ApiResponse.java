package com.Alchive.backend.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    public ApiResponse(int code, String message, Object response) {
        this.code = code;
        this.message = message;
        this.data = response;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
