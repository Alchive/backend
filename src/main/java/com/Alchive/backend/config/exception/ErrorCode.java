package com.Alchive.backend.config.exception;

public interface ErrorCode {
//    String name();
    int getHttpStatusCode();
    String getMessage();
}
