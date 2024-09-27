package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.dto.response.SolutionResponseDTO;
import com.Alchive.backend.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.SOLUTION_UPDATE_SUCCESS;

@Tag(name = "풀이", description = "풀이 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/solutions")
public class SolutionController {
    private final SolutionService solutionService;

    @Operation(summary = "풀이 수정", description = "풀이의 solution을 수정하는 메서드입니다. ")
    @PatchMapping("/{solutionId}")
    public ResponseEntity<ResultResponse> updateSolution(HttpServletRequest tokenRequest, @PathVariable Long solutionId, @RequestBody @Valid SolutionUpdateRequest solutionUpdateRequest) {
        SolutionResponseDTO solution = solutionService.updateSolution(tokenRequest, solutionId, solutionUpdateRequest);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_UPDATE_SUCCESS, solution));
    }
}
