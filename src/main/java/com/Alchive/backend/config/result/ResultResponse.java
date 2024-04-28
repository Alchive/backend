package com.Alchive.backend.config.result;

import lombok.Getter;

@Getter
public class ResultResponse {
    private String code;
    private String message;
    private Object data;

    public ResultResponse(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResultResponse(ResultCode resultCode, String message, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage() + " - " + message;
        this.data = data;
    }

    public static ResultResponse of(ResultCode resultCode, String message, Object data) {
        return new ResultResponse(resultCode, message, data);
    }

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public static ResultResponse of(ResultCode resultCode) {
        return new ResultResponse(resultCode, "");
    }
}