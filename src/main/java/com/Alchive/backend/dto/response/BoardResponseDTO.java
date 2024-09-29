package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.board.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BoardResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String memo;
    private BoardStatus status;
    private String description;

    public BoardResponseDTO(Board board) {
        this.id = board.getId();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.memo = board.getMemo();
        this.status = board.getStatus();
        this.description = board.getDescription();
    }
}
