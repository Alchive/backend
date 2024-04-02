package com.Alchive.backend.controller;

import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemListDTO;
import com.Alchive.backend.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "문제", description = "문제 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/problems") // 공통 api
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "플랫폼 별 문제 목록 조회 메서드", description = "특정 플랫폼에 해당하는 문제 목록을 조회하는 메서드입니다. ")
    @GetMapping("/platform/{platform}")
    public ResponseEntity<ApiResponse> getProblemPlatform(@PathVariable String platform) {
        List<ProblemListDTO> problemData = problemService.getProblemsByPlatform(platform);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "플랫폼 별 문제 목록을 불러왔습니다.", problemData));
    }

    @Operation(summary = "모든 문제 목록 조회 메서드", description = "모든 문제 목록을 조회하는 메서드입니다.")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProblems() {
        List<ProblemListDTO> problemData = problemService.getAllProblems();
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "모든 문제 목록을 불러왔습니다.", problemData));
    }
}