package com.Alchive.backend.config.error;

public interface ErrorCode {
    int getHttpStatusCode();
    String getMessage();
}
