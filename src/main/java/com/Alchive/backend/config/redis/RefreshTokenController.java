package com.Alchive.backend.config.redis;

import com.Alchive.backend.config.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;

@RestController
@Tag(name = "리프레시 토큰", description = "[Test] 리프레시 토큰 관련 api입니다. ")
@RequestMapping("/api/v2/refreshTokens")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "리프레시 토큰 저장 메서드", description = "리프레시 토큰을 Redis에 저장하는 메서드입니다. ")
    @PostMapping("")
    public ResponseEntity<ResultResponse> saveRefreshToken(@RequestBody CreateRefreshTokenRequest request) {
        refreshTokenService.saveRefreshToken(request.getEmail(), request.getRefreshToken());
        return ResponseEntity.ok(ResultResponse.of(TOKEN_REFRESH_SUCCESS, request.getRefreshToken()));
    }

    @Operation(summary = "리프레시 토큰 검색 메서드", description = "이메일로 리프레시 토큰을 가져오는 메서드입니다. ")
    @GetMapping("")
    public ResponseEntity<ResultResponse> getResfreshToken(@RequestParam String email) {
        String refreshToken = refreshTokenService.getRefreshToken(email);
        return ResponseEntity.ok(ResultResponse.of(TOKEN_REFRESH_SUCCESS, refreshToken));
    }

    @Operation(summary = "리프레시 토큰 재발급 메서드", description = "이메일로 리프레시 토큰을 재발급하는 메서드입니다. ")
    @GetMapping("/newToken")
    public ResponseEntity<ResultResponse> createRefreshToken(@RequestParam String email) {
        String refreshToken = refreshTokenService.createRefreshToken(email);
        return ResponseEntity.ok(ResultResponse.of(TOKEN_REFRESH_SUCCESS, refreshToken));
    }
}
