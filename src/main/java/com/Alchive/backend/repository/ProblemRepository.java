package com.Alchive.backend.repository;

import com.Alchive.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByProblemPlatform(String problemPlatform);
    List<Problem> findByUserUserId(Long userId);
}
