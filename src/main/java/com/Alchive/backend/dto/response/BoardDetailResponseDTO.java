package com.Alchive.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class BoardDetailResponseDTO {
    private BoardResponseDTO board;
    private ProblemResponseDTO problem;
    private List<SolutionResponseDTO> solutions;

    public BoardDetailResponseDTO(BoardResponseDTO boardResponseDTO, ProblemResponseDTO problemResponseDTO, List<SolutionResponseDTO> solutions) {
        this.board = boardResponseDTO;
        this.problem = problemResponseDTO;
        this.solutions = solutions;
    }
}
