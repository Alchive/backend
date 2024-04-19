package com.Alchive.backend.controller;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchProblemException;
import com.Alchive.backend.dto.request.ProblemCreateRequest;
import com.Alchive.backend.dto.request.SubmitProblemCreateRequest;
import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.ProblemDetailResponseDTO;
import com.Alchive.backend.dto.response.ProblemListResponseDTO;
import com.Alchive.backend.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "문제", description = "문제 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/problems") // 공통 api
public class ProblemController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ProblemService problemService;

    @Operation(summary = "미제출 문제 저장 메서드", description = "코드 없이 문제 설명 페이지에서 가져온 문제 정보만을 저장하는 메서드입니다.")
    @PostMapping
    public ResponseEntity<ApiResponse> createProblem(
            @RequestParam Long userId,
            @RequestBody @Valid ProblemCreateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("valide error");
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String message = objectError.getDefaultMessage();
                sb.append("field :" + field.getField());
                sb.append("message :" + message);
            });
            log.error(sb.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), sb.toString()));
        }
        problemService.createProblem(userId, request);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "미제출 문제 정보를 저장했습니다."));
    }

    @Operation(summary = "제출 후(맞/틀) 문제 저장 메서드", description = "코드 제출 후 문제 정보와 정답 여부, 코드 정보를 저장하는 메서드입니다.")
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> createProblemSubmit(
            @RequestParam Long userId,
            @RequestBody @Valid SubmitProblemCreateRequest request) {
        problemService.createProblemSubmit(userId, request);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "제출한 문제와 코드 정보를 저장했습니다."));
    }

    @Operation(summary = "문제 저장 여부 검사 메서드", description = "문제 번호를 이용해 저장된 문제인지를 검사하는 메서드입니다.")
    @GetMapping("/check/{problemNumber}")
    public ResponseEntity<ApiResponse> checkProblem(@RequestParam Long userId, @PathVariable int problemNumber, @RequestParam String platform) {
        if (problemService.checkProblem(userId, problemNumber, platform)) { // 존재하는 경우
            return ResponseEntity.ok()
                    .body(new ApiResponse(HttpStatus.OK.value(), "저장된 문제입니다."));
        } else {
            throw new NoSuchProblemException(Code.PROBLEM_NOT_FOUND, problemNumber);
        }
    }

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
            @RequestParam(required = false, name = "c") @Schema(description = "카테고리")
            String category
    ) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsSearch(userId, keyword, category);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "검색 결과를 불러왔습니다.", problemData));
    }

    @Operation(summary = "문제 목록 조회 메서드", description = "문제 목록을 조회하는 메서드입니다.")
    @GetMapping
    public ResponseEntity<ApiResponse> getProblemsByUserId(@RequestParam Long userId) {
        List<ProblemListResponseDTO> problemData = problemService.getProblemsByUserId(userId);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "문제 목록을 불러왔습니다.", problemData));
    }

    @Operation(summary = "문제 삭제 메서드", description = "문제 정보를 삭제하는 메서드입니다. ")
    @DeleteMapping("/{problemId}")
    public ResponseEntity<ApiResponse> deleteProblem(@PathVariable Long problemId) {
        problemService.deleteProblem(problemId);
        return ResponseEntity.ok()
                .body(new ApiResponse((HttpStatus.OK.value()),"문제를 삭제했습니다. "));
    }

    @Operation(summary = "단일 문제 조회 메서드", description = "특정 문제를 조회하는 메서드입니다.")
    @GetMapping("/{problemId}")
    public ResponseEntity<ApiResponse> getProblemByProblemId(@PathVariable @Schema(description = "문제 아이디") Long problemId) {
        ProblemDetailResponseDTO problem = problemService.getProblemByProblemId(problemId);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "문제를 조회했습니다.", problem));
    }

    @Operation(summary = "문제 메모 수정 메서드", description = "특정 문제의 메모를 수정하는 메서드 입니다.\n\n메모는 큰따옴표를 해제하고 작성해서 테스트 해주세요.")
    @PutMapping("/memo/{problemId}")
    public ResponseEntity<ApiResponse> updateProblemMemo(HttpServletRequest tokenRequest,
                                                         @PathVariable @Schema(description = "문제 아이디") Long problemId,
                                                         @RequestBody @Schema(description = "메모 내용 - 큰따옴표를 해제하고 작성해주세요.") String problemMemo) {
        problemService.updateProblemMemo(tokenRequest, problemId, problemMemo);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "메모를 수정했습니다."));
    }
}