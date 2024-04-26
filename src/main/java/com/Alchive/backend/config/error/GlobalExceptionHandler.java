package com.Alchive.backend.config.error;

import com.Alchive.backend.config.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleRuntimeException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String paramType1 = exception.getParamType1();
        Object param1 = exception.getParam1();
        String paramType2 = exception.getParamType2();
        Object param2 = exception.getParam2();
        return handleExceptionInternal(errorCode, paramType1, param1, paramType2, param2);
    }


    // 에러 코드를 전달받아 에러 메시지 작성
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String paramType1, Object param1, String paramType2, Object param2) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, paramType1, param1, paramType2, param2));
    }

    // 반환할 에러 메시지 정의
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String paramType1, Object param1, String paramType2, Object param2) {
        if (paramType2 == null && param2 == null) {
            return ErrorResponse.builder()
                    .code(String.valueOf(errorCode.getCode()))
                    .message(errorCode.getMessage() + " " + paramType1 + ": " + param1)
                    .build();
        } else if (paramType2 == null) {
            return ErrorResponse.builder()
                    .code(String.valueOf(errorCode.getCode()))
                    .message(errorCode.getMessage() + " " + paramType1 + ": (" + param1 + ")" + param2)
                    .build();
        } else {
            return ErrorResponse.builder()
                    .code(String.valueOf(errorCode.getCode()))
                    .message(errorCode.getMessage() + " " + paramType1 + ": " + param1 + ", " + paramType2 + ": " + param2)
                    .build();
        }
    }
}
