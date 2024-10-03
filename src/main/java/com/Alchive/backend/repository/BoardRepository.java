package com.Alchive.backend.repository;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByProblem_PlatformAndProblem_NumberAndUser_Id(ProblemPlatform platform, int problemNumber, Long userId);
}
