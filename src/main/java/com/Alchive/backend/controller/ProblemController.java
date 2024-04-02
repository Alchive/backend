package com.Alchive.backend.controller;

import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문제", description = "문제 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/problems") // 공통 api
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "플랫폼 별 문제 목록 조회 메서드", description = "특정 플랫폼에 해당하는 문제 목록을 조회하는 메서드입니다.")
    @GetMapping("/platform")
    public ResponseEntity<ApiResponse> getProblemPlatform(
            @RequestParam(required = true, name = "id") @Schema(description = "사용자 아이디")
            Long userId,
            @RequestParam(required = true, name = "p") @Schema(description = "Algorithm Platform")
            String platform) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsByPlatform(userId, platform);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "플랫폼 별 문제 목록을 불러왔습니다.", problemData));
    }

    @Operation(summary = "문제 검색 메서드", description = "특정 키워드에 대한 문제를 검색하는 메서드입니다. ")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getProblemSearch(
            @RequestParam(required = true, name = "id") @Schema(description = "사용자 아이디")
            Long userId,
            @RequestParam(required = true, name = "k") @Schema(description = "검색 내용")
            String keyword,
            @RequestParam(required = true, name = "c") @Schema(description = "카테고리")
            String category
            ) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsSearch(userId, keyword, category);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "검색 결과를 불러왔습니다.", problemData));
    }
}