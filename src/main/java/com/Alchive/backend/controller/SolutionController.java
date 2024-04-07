package com.Alchive.backend.controller;

import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "풀이", description = "풀이 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users") // 공통 url
public class SolutionController {

    private final SolutionService solutionService;

    @Operation(summary = "풀이 수정", description = "풀이의 solution을 수정하는 메서드입니다. ")
    @PutMapping("/solution")
    public ResponseEntity<ApiResponse> updateSolution(@RequestBody SolutionUpdateRequest request) {
        solutionService.updateSolution(request);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "풀이를 수정했습니다."));
    }
}
