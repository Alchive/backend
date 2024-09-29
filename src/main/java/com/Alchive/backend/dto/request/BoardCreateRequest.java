package com.Alchive.backend.dto.request;

import com.Alchive.backend.domain.board.BoardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardCreateRequest {
    @NotNull(message = "문제 정보는 필수입니다. ")
    private ProblemCreateRequest problemCreateRequest;
    private String memo;
    private String description;
    @NotNull(message = "게시물 상태는 필수입니다. ")
    private BoardStatus status;
}
