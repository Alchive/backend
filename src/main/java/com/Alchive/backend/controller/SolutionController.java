package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SolutionCreateRequest;
import com.Alchive.backend.dto.request.SolutionUpdateRequest;
import com.Alchive.backend.dto.response.SolutionDetailResponseDTO;
import com.Alchive.backend.service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;

@Tag(name = "풀이", description = "풀이 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/solutions")
public class SolutionController {
    private final SolutionService solutionService;

    @Operation(summary = "풀이 생성", description = "새로운 풀이를 생성하는 메서드입니다.")
    @PostMapping("/{boardId}")
    public ResponseEntity<ResultResponse> createSolution(@PathVariable Long boardId, @RequestBody @Valid SolutionCreateRequest solutionRequest) {
        SolutionDetailResponseDTO solution = solutionService.createSolution(boardId, solutionRequest);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_CREATE_SUCCESS, solution));
    }

    @Operation(summary = "풀이 수정", description = "풀이 내용을 수정하는 메서드입니다. ")
    @PatchMapping("/{solutionId}")
    public ResponseEntity<ResultResponse> updateSolution(@AuthenticationPrincipal User user, @PathVariable Long solutionId, @RequestBody @Valid SolutionUpdateRequest solutionRequest) {
        SolutionDetailResponseDTO solution = solutionService.updateSolution(user, solutionId, solutionRequest);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_UPDATE_SUCCESS, solution));
    }

    @Operation(summary = "풀이 삭제", description = "풀이를 삭제하는 메서드입니다.")
    @DeleteMapping("/{solutionId}")
    public ResponseEntity<ResultResponse> deleteSolution(@AuthenticationPrincipal User user, @PathVariable Long solutionId) {
        solutionService.deleteSolution(user, solutionId);
        return ResponseEntity.ok(ResultResponse.of(SOLUTION_DELETE_SUCCESS));
    }
}
