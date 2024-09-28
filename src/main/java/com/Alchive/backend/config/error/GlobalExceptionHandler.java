package com.Alchive.backend.config.error;

import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        log.info(exception.getMessage() + ", " + exception.getClass());
        ErrorCode errorCode = ErrorCode._INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(errorCode.getCode()))
                        .message(errorCode.getMessage() + ": " + exception.getClass())
                        .build());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleRuntimeException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(errorCode.getCode()))
                        .message(errorCode.getMessage())
                        .build());
    }
}
