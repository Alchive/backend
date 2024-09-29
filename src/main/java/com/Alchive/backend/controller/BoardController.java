package com.Alchive.backend.controller;

import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.request.BoardMemoUpdateRequest;
import com.Alchive.backend.dto.response.BoardDetailResponseDTO;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.Alchive.backend.config.result.ResultCode.*;

@Slf4j
@Tag(name = "게시물", description = "게시물 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성하는 메서드입니다. ")
    @PostMapping("")
    public ResponseEntity<ResultResponse> createBoard(HttpServletRequest tokenRequest, @RequestBody @Valid BoardCreateRequest boardCreateRequest) {
        BoardResponseDTO board = boardService.createBoard(tokenRequest, boardCreateRequest);
        return ResponseEntity.ok(ResultResponse.of(BOARD_CREATE_SUCCESS, board));
    }

    @Operation(summary = "게시물 조회", description = "게시물 정보를 조회하는 메서드입니다. ")
    @GetMapping("/{boardId}")
    public  ResponseEntity<ResultResponse> getBoard(HttpServletRequest tokenRequest, @PathVariable Long boardId) {
        BoardDetailResponseDTO board = boardService.getBoardDetail(tokenRequest, boardId);
        return ResponseEntity.ok(ResultResponse.of(BOARD_DETAIL_INFO_SUCCESS, board));
    }

    @Operation(summary = "게시물 메모 업데이트", description = "게시물 메모를 수정하는 메서드입니다. ")
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResultResponse> updateBoardMemo(HttpServletRequest tokenRequest, @PathVariable Long boardId, @RequestBody BoardMemoUpdateRequest updateRequest) {
        BoardResponseDTO board = boardService.updateBoardMemo(tokenRequest, boardId, updateRequest);
        return ResponseEntity.ok(ResultResponse.of(BOARD_MEMO_UPDATE_SUCCESS, board));
    }

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제하는 메서드입니다. ")
    @DeleteMapping ("/{boardId}")
    public ResponseEntity<ResultResponse> deleteBoard(HttpServletRequest tokenRequest, @PathVariable Long boardId) {
        boardService.deleteBoard(tokenRequest, boardId);
        return ResponseEntity.ok(ResultResponse.of(BOARD_DELETE_SUCCESS));
    }
}