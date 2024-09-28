package com.Alchive.backend.repository;

import com.Alchive.backend.domain.problem.Problem;
import com.Alchive.backend.domain.problem.ProblemPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    boolean existsByNumberAndPlatform(Integer number, ProblemPlatform platform);

    Problem findByNumberAndPlatform(Integer number, ProblemPlatform platform);
}
