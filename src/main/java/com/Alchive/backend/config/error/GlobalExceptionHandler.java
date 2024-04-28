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
        String message = exception.getMessage();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(errorCode.getCode()))
                        .message(errorCode.getMessage() + message)
                        .build());
    }
}
