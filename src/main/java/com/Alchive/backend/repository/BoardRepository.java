package com.Alchive.backend.repository;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByProblem_PlatformAndProblem_NumberAndUser_Id(ProblemPlatform platform, int problemNumber, Long userId);


    @Query(value = "SELECT * FROM Board WHERE createdAt <= :threeDaysAgo AND status = 'NOT_SUBMITTED' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Board findUnsolvedBoardAddedBefore(@Param("threeDaysAgo") LocalDateTime threeDaysAgo);
}
