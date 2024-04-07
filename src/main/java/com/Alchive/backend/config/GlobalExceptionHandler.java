package com.Alchive.backend.config;

import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.config.exception.NoSuchUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // NoSuchElementException 예외를 처리하는 ExceptionHandler
    @ExceptionHandler(NoSuchIdException.class) // NoSuchIdException 발생했을 때 처리됨
    public ResponseEntity<Object> handleNoSuchUserException(NoSuchIdException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        Long userId = exception.getId();
        return handleExceptionInternal(errorCode, userId);
    }

    @ExceptionHandler(NoSuchPlatformException.class) // NoSuchPlatformException 발생했을 때 처리됨
    public ResponseEntity<Object> handleNoSuchPlatformException(NoSuchPlatformException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String platform = exception.getPlatform();
        return handleExceptionInternal(errorCode, platform);
    }

    @ExceptionHandler(NoSuchUserException.class) // NoSuchUserException 발생했을 때 처리됨
    public ResponseEntity<Object> handleNoSuchUserException(NoSuchUserException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String user = exception.getUser();
        return handleExceptionInternal(errorCode, user);
    }

    // 에러 코드를 전달받아 에러 메시지 작성
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, Object params) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(makeErrorResponse(errorCode, params));
    }

    // 매개변수를 받지 않는 경우
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(makeErrorResponse(errorCode));
    }

    // 반환할 에러 메시지 정의
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, Object params) {
        return ErrorResponse.builder()
                .code(String.valueOf(errorCode.getHttpStatusCode()))
                .message(errorCode.getMessage() + params + "]")
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(String.valueOf(errorCode.getHttpStatusCode()))
                .message(errorCode.getMessage())
                .build();
    }
}
