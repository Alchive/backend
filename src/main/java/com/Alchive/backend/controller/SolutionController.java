package com.Alchive.backend.controller;

import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.dto.request.SolutionRequest;
import com.Alchive.backend.dto.response.SolutionDetailResponseDTO;
import com.Alchive.backend.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;

@Tag(name = "풀이", description = "풀이 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/solutions")
public class SolutionController {
    private final SolutionService solutionService;
    private final TokenService tokenService;

    @Operation(summary = "풀이 생성", description = "새로운 풀이를 생성하는 메서드입니다.")
    @PostMapping("/{boardId}")
    public ResponseEntity<ResultResponse> createSolution(HttpServletRequest tokenRequest, @PathVariable Long boardId, @RequestBody @Valid SolutionRequest solutionRequest) {
        tokenService.validateAccessToken(tokenRequest);
        SolutionDetailResponseDTO solution = solutionService.createSolution(boardId, solutionRequest);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_CREATE_SUCCESS, solution));
    }

    @Operation(summary = "풀이 수정", description = "풀이 내용을 수정하는 메서드입니다. ")
    @PatchMapping("/{solutionId}")
    public ResponseEntity<ResultResponse> updateSolution(HttpServletRequest tokenRequest, @PathVariable Long solutionId, @RequestBody @Valid SolutionRequest solutionRequest) {
        tokenService.validateAccessToken(tokenRequest);
        SolutionDetailResponseDTO solution = solutionService.updateSolution(solutionId, solutionRequest);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_UPDATE_SUCCESS, solution));
    }

    @Operation(summary = "풀이 삭제", description = "풀이를 삭제하는 메서드입니다.")
    @DeleteMapping("/{solutionId}")
    public ResponseEntity<ResultResponse> deleteSolution(HttpServletRequest tokenRequest, @PathVariable Long solutionId) {
        tokenService.validateAccessToken(tokenRequest);
        SolutionDetailResponseDTO solution = solutionService.deleteSolution(solutionId);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_DELETE_SUCCESS, solution));
    }
}
