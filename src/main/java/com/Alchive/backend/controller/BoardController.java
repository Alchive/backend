package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.request.BoardMemoUpdateRequest;
import com.Alchive.backend.dto.request.BoardUpdateRequest;
import com.Alchive.backend.dto.request.ProblemNumberRequest;
import com.Alchive.backend.dto.response.BoardDetailResponseDTO;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.service.BoardService;
import com.Alchive.backend.service.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.Alchive.backend.config.result.ResultCode.*;

@Slf4j
@Tag(name = "게시물", description = "게시물 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/boards")
public class BoardController {
    private final BoardService boardService;
    private final SlackService slackService;

    @Operation(summary = "게시물 저장 여부 조회", description = "게시물의 저장 여부를 조회하는 메서드입니다. ")
    @PostMapping("/saved")
    public ResponseEntity<ResultResponse> isBoardSaved(@AuthenticationPrincipal User user, @RequestBody @Valid ProblemNumberRequest problemNumberRequest) {
        BoardDetailResponseDTO board = boardService.isBoardSaved(user, problemNumberRequest);
        if (board != null) {
            return ResponseEntity.ok(ResultResponse.of(BOARD_INFO_SUCCESS, board));
        } else {
            return ResponseEntity.ok(ResultResponse.of(BOARD_NOT_EXIST, null));
        }
    }

    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회하는 메서드입니다. ")
    @GetMapping("")
    public ResponseEntity<ResultResponse> getBoardList(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Page<List<BoardDetailResponseDTO>> boardList = boardService.getBoardList(offset, limit);
        return ResponseEntity.ok(ResultResponse.of(BOARD_LIST_INFO_SUCCESS, boardList));
    }

    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성하는 메서드입니다. ")
    @PostMapping("")
    public ResponseEntity<ResultResponse> createBoard(@AuthenticationPrincipal User user, @RequestBody @Valid BoardCreateRequest boardCreateRequest) {
        BoardResponseDTO board = boardService.createBoard(user, boardCreateRequest);
//        slackService.sendMessageCreateBoard(boardCreateRequest, board);
        return ResponseEntity.ok(ResultResponse.of(BOARD_CREATE_SUCCESS, board));
    }

    @Operation(summary = "게시물 조회", description = "게시물 정보를 조회하는 메서드입니다. ")
    @GetMapping("/{boardId}")
    public ResponseEntity<ResultResponse> getBoard(@PathVariable Long boardId) {
        BoardDetailResponseDTO board = boardService.getBoardDetail(boardId);
        return ResponseEntity.ok(ResultResponse.of(BOARD_INFO_SUCCESS, board));
    }

    @Operation(summary = "게시물 업데이트", description = "게시물 설명을 수정하는 메서드입니다. ")
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResultResponse> updateBoard(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody BoardUpdateRequest updateRequest) {
        BoardResponseDTO board = boardService.updateBoard(user, boardId, updateRequest);
        return ResponseEntity.ok(ResultResponse.of(BOARD_MEMO_UPDATE_SUCCESS, board));
    }

    @Operation(summary = "게시물 삭제", description = "게시물을 삭제하는 메서드입니다. ")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResultResponse> deleteBoard(@AuthenticationPrincipal User user, @PathVariable Long boardId) {
        boardService.deleteBoard(user, boardId);
        return ResponseEntity.ok(ResultResponse.of(BOARD_DELETE_SUCCESS));
    }

    @Operation(summary = "게시물 메모 업데이트", description = "게시물 메모를 수정하는 메서드입니다. ")
    @PatchMapping("/memo/{boardId}")
    public ResponseEntity<ResultResponse> updateBoardMemo(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody BoardMemoUpdateRequest updateRequest) {
        BoardResponseDTO board = boardService.updateBoardMemo(user, boardId, updateRequest);
        return ResponseEntity.ok(ResultResponse.of(BOARD_MEMO_UPDATE_SUCCESS, board));
    }
}