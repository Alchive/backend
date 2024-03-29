package com.Alchive.backend.controller;

import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemListDTO;
import com.Alchive.backend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/problems") // 공통 api
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/platform/{platform}")
    public ResponseEntity<ApiResponse> getProblemPlatform(@PathVariable String platform) {
        List<ProblemListDTO> problemData = problemService.getProblemsByPlatform(platform);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "플랫폼 별 문제 목록을 불러왔습니다.", problemData));
    }

}