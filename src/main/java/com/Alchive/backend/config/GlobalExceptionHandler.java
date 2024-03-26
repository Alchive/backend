package com.Alchive.backend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // NoSuchElementException 예외를 처리하는 ExceptionHandler
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleNoSuchElementException(GeneralException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        Long userId = exception.getUserId();
        return handleExceptionInternal(errorCode,userId);
    }

    // 에러 코드를 전달받아 에러 메시지 작성
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, Long userId) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(makeErrorResponse(errorCode,userId));
    }

    // 반환할 에러 메시지 정의
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, Long userId) {
        return ErrorResponse.builder()
                .code(String.valueOf(errorCode.getHttpStatusCode()))
                .message(errorCode.getMessage()+"[id: "+userId+"]")
                .build();
    }

    // 아래에 자유롭게 처리할 예외 핸들러 추가 가능
    // 예시: userId를 매개변수로 받지 않는 경우
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(String.valueOf(errorCode.getHttpStatusCode()))
                .message(errorCode.getMessage())
                .build();
    }
}
