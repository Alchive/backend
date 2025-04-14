package com.Alchive.backend.config.redis;

import lombok.Getter;

@Getter
public class CreateRefreshTokenRequest {
    private String email;
    private String refreshToken;
}
