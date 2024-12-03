package com.Alchive.backend.config.jwt;

import com.Alchive.backend.config.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.Alchive.backend.config.result.ResultCode.*;

@Tag(name = "JWT", description = "[Test] JWT 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/jwt") // 공통 url
public class JwtController {
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "액세스 토큰 재발급 메서드", description = "액세스 토큰을 재발급하는 메서드입니다.")
    @GetMapping("/access")
    public ResponseEntity<ResultResponse> createAccessToken(String email) {
        String accessToken = jwtTokenProvider.createAccessToken(email);
        return ResponseEntity.ok(ResultResponse.of(TOKEN_ACCESS_SUCCESS, accessToken));
    }
}
