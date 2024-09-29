package com.Alchive.backend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class BoardDetailResponseDTO {
    private BoardResponseDTO boardResponseDTO;
    private ProblemResponseDTO problemResponseDTO;
    private List<SolutionResponseDTO> solutions;

    public BoardDetailResponseDTO(BoardResponseDTO boardResponseDTO, ProblemResponseDTO problemResponseDTO, List<SolutionResponseDTO> solutions) {
        this.boardResponseDTO = boardResponseDTO;
        this.problemResponseDTO = problemResponseDTO;
        this.solutions = solutions;
    }
}
