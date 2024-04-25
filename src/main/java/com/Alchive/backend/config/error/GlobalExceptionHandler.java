package com.Alchive.backend.config.error;

import com.Alchive.backend.config.error.exception.problem.NoSuchPlatformException;
import com.Alchive.backend.config.error.exception.problem.NoSuchProblemIdException;
import com.Alchive.backend.config.error.exception.token.TokenExpiredException;
import com.Alchive.backend.config.error.exception.token.TokenNotFoundException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // algorithm


    // problem
    @ExceptionHandler(NoSuchPlatformException.class) // 찾으려는 platform이 존재하지 않는 경우
    public ResponseEntity<Object> handleNoSuchPlatformException(NoSuchPlatformException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String platform = exception.getPlatform();
        return handleExceptionInternal(errorCode, platform);
    }
    @ExceptionHandler(NoSuchProblemIdException.class) // 찾으려는 problemId가 존재하지 않는 경우
    public ResponseEntity<Object> handleNoSuchProblemException(NoSuchProblemIdException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        int problemId = exception.getProblemId();
        return handleExceptionInternal(errorCode, problemId);
    }

    // solution


    // token
    @ExceptionHandler(TokenExpiredException.class) // token이 만료된 경우
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException exception)
    {
        ErrorCode errorCode = exception.getErrorCode();
        String token = exception.getToken();
        return handleExceptionInternal(errorCode, token);
    }

    @ExceptionHandler(TokenNotFoundException.class) // token이 존재하지 않는 경우
    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException exception) { // token이 존재하지 않는 경우
        ErrorCode errorCode = exception.getErrorCode();
        String tokenType = exception.getTokenType();
        return handleExceptionInternal(errorCode, tokenType);
    }

    // user
    @ExceptionHandler(NoSuchUserIdException.class) // 찾으려는 id가 존재하지 않는 경우
    public ResponseEntity<Object> handleNoSuchUserException(NoSuchUserIdException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        Long userId = exception.getUserId();
        return handleExceptionInternal(errorCode, userId);
    }

    @ExceptionHandler(NoSuchUserException.class) // 찾으려는 user가 존재하지 않는 경우
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
