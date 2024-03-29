package com.Alchive.backend.config;

public interface ErrorCode {
//    String name();
    int getHttpStatusCode();
    String getMessage();
}
