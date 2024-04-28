package com.Alchive.backend.config.error.exception;

import com.Alchive.backend.config.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;
//    private final String paramType1;
//    private final Object param1;
//    private String paramType2 = null;
//    private Object param2 = null;

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = message;
    }
//    public BusinessException(ErrorCode errorCode, String paramType, Object param) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//        this.paramType1 = paramType;
//        this.param1 = param;
//    }
//
//    public BusinessException(ErrorCode errorCode, String paramType1, Object param1, Object param2) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//        this.paramType1 = paramType1;
//        this.param1 = param1;
//        this.param2 = param2;
//    }
//
//    public BusinessException(ErrorCode errorCode, String paramType1, Object param1, String  paramType2, Object param2) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//        this.paramType1 = paramType1;
//        this.param1 = param1;
//        this.paramType2 = paramType2;
//        this.param2 = param2;
//    }
}
