package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.dto.response.SnsResponseDTO;
import com.Alchive.backend.service.SnsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;

@Tag(name = "소셜", description = "소셜 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/sns")
public class SnsController {
    private final SnsService snsService;

    @Operation(summary = "소셜 정보 조회", description = "소셜 정보를 조회하는 메서드입니다. ")
    @GetMapping("/{snsId}")
    public ResponseEntity<ResultResponse> getSns(@PathVariable Long snsId) {
        SnsResponseDTO sns = snsService.getSns(snsId);
        return ResponseEntity.ok(ResultResponse.of(SNS_INFO_SUCCESS, sns));
    }

    @Operation(summary = "소셜 정보 생성", description = "소셜 정보를 생성하는 메서드입니다. ")
    @PostMapping("")
    public ResponseEntity<ResultResponse> createSns(HttpServletRequest tokenRequest, SnsCreateRequest request) {
        snsService.createSns(tokenRequest, request);
        return ResponseEntity.ok(ResultResponse.of(SNS_CREATE_SUCCESS));
    }
}
