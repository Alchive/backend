package com.Alchive.backend.repository;

import com.Alchive.backend.domain.algorithmProblem.AlgorithmProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmProblemRepository extends JpaRepository<AlgorithmProblem, Long> {
    @Query("SELECT a.name FROM AlgorithmProblem ap JOIN ap.algorithm a WHERE ap.problem.id = :problemId")
    List<String> findAlgorithmNamesByProblemId(@Param("problemId") Long problemId);
}
