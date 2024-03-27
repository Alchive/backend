package com.Alchive.backend.config;

import org.springframework.http.HttpStatusCode;

public interface ErrorCode {
    String name();
    HttpStatusCode getHttpStatusCode();
    String getMessage();
}
