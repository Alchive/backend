package com.Alchive.backend.controller;

import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemListDTO;
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
    @GetMapping("/platform/{platform}")
    public ResponseEntity<ApiResponse> getProblemPlatform(@RequestParam(required = true, name = "p") @Schema(description = "Algorithm Platform") String platform) {
        List<ProblemListDTO> problemData = problemService.getProblemsByPlatform(platform);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "플랫폼 별 문제 목록을 불러왔습니다.", problemData));
    }

    @Operation(summary = "문제 검색 메서드", description = "특정 키워드에 대한 문제를 검색하는 메서드입니다. ")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse> getProblemSearch(@PathVariable @Schema(description = "Keyword") String keyword, @RequestParam(required = false, name = "c") @Schema(description = "Category(number or title) 분류 용입니다. 작성하지 않으면 문제번호와 제목을 모두 검색합니다.") String category) {
        List<ProblemListDTO> problemData = problemService.getProblemsSearch(keyword, category);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "검색 결과를 불러왔습니다.", problemData));
    }
}