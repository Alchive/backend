package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.ProblemMemoUpdateRequest;
import com.Alchive.backend.dto.response.ProblemDetailResponseDTO;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.Alchive.backend.config.result.ResultCode.*;


@Tag(name = "문제", description = "문제 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/problems") // 공통 api
public class ProblemController {
    private final ProblemService problemService;

    @Operation(summary = "미제출 문제 저장 메서드", description = "코드 없이 문제 설명 페이지에서 가져온 문제 정보만을 저장하는 메서드입니다.")
    @PostMapping
    public ResponseEntity<ResultResponse> createProblem(
            HttpServletRequest tokenRequest,
            @RequestBody @Valid ProblemCreateRequest problemRequest, BindingResult bindingResult) {
        problemService.createProblem(tokenRequest, problemRequest);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_UNSUBMIT_SAVE_SUCCESS));
    }

    @Operation(summary = "제출 후(맞/틀) 문제 저장 메서드", description = "코드 제출 후 문제 정보와 정답 여부, 코드 정보를 저장하는 메서드입니다.")
    @PostMapping("/submit")
    public ResponseEntity<ResultResponse> createProblemSubmit(
            HttpServletRequest tokenRequest,
            @RequestBody @Valid ProblemCreateRequest problemRequest) {
        problemService.createProblemSubmit(tokenRequest, problemRequest);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_SUBMIT_SAVE_SUCCESS));
    }

    @Operation(summary = "문제 저장 여부 검사 메서드", description = "문제 번호를 이용해 저장된 문제인지를 검사하는 메서드입니다.")
    @GetMapping("/check/{problemNumber}")
    public ResponseEntity<ResultResponse> checkProblem(HttpServletRequest tokenRequest, @PathVariable int problemNumber, @RequestParam String platform) {
        if (problemService.checkProblem(tokenRequest, problemNumber, platform)) { // 존재하는 경우
            return ResponseEntity.ok(ResultResponse.of(PROBLEM_DUPLICATED, true));
        }
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_NOT_DUPLICATED, false));
    }

    @Operation(summary = "플랫폼 별 문제 목록 조회 메서드", description = "특정 플랫폼에 해당하는 문제 목록을 조회하는 메서드입니다.")
    @GetMapping("/platform")
    public ResponseEntity<ResultResponse> getProblemPlatform(
            HttpServletRequest tokenRequest,
            @RequestParam(required = true, name = "p") @Schema(description = "Algorithm Platform")
            String platform) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsByPlatform(tokenRequest, platform);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_PLATFORM_LIST_SUCCESS, platform, problemData));
    }

    @Operation(summary = "문제 검색 메서드", description = "특정 키워드에 대한 문제를 검색하는 메서드입니다. ")
    @GetMapping("/search")
    public ResponseEntity<ResultResponse> getProblemSearch(
            HttpServletRequest tokenRequest,
            @RequestParam(required = true, name = "k") @Schema(description = "검색 내용")
            String keyword,
            @RequestParam(required = false, name = "c") @Schema(description = "카테고리")
            String category
            ) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsSearch(tokenRequest, keyword, category);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_SEARCH_SUCCESS, problemData));
    }

    @Operation(summary = "문제 목록 조회 메서드", description = "문제 목록을 조회하는 메서드입니다.")
    @GetMapping
    public ResponseEntity<ResultResponse> getProblemsByUserId(HttpServletRequest tokenRequest) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsByUserId(tokenRequest);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_LIST_SUCCESS, problemData));
    }

    @Operation(summary = "문제 삭제 메서드", description = "문제 정보를 삭제하는 메서드입니다. ")
    @DeleteMapping("/{problemId}")
    public ResponseEntity<ResultResponse> deleteProblem(HttpServletRequest tokenRequest, @PathVariable Long problemId) {
        problemService.deleteProblem(tokenRequest, problemId);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_DELETE_SUCCESS));
    }

    @Operation(summary = "단일 문제 조회 메서드", description = "특정 문제를 조회하는 메서드입니다.")
    @GetMapping("/{problemId}")
    public ResponseEntity<ResultResponse> getProblemByProblemId(@PathVariable @Schema(description = "문제 아이디") Long problemId) {
        ProblemDetailResponseDTO problem = problemService.getProblemByProblemId(problemId);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_DETAIL_INFO_SUCCESS, problem));
    }

    @Operation(summary = "문제 메모 수정 메서드", description = "특정 문제의 메모를 수정하는 메서드 입니다.")
    @PutMapping("/memo")
    public ResponseEntity<ResultResponse> updateProblemMemo(HttpServletRequest tokenRequest, @RequestBody ProblemMemoUpdateRequest memoRequest) {
        problemService.updateProblemMemo(tokenRequest, memoRequest);
        return ResponseEntity.ok(ResultResponse.of(PROBLEM_MEMO_UPDATE_SUCCESS));
    }
}