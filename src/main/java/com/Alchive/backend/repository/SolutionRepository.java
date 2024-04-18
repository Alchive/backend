package com.Alchive.backend.repository;

import com.Alchive.backend.domain.Solution;
import com.Alchive.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Optional<Solution> findBySolutionId(Long solutionId);
    Optional<Solution> findByProblemProblemId(Long problemId);
}