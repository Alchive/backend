package com.Alchive.backend.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    @Indexed
    private String email;
    private String refreshToken;
}
