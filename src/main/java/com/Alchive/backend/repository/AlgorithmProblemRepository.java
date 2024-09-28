package com.Alchive.backend.repository;

import com.Alchive.backend.domain.algorithmProblem.AlgorithmProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmProblemRepository extends JpaRepository<AlgorithmProblem, Long> {
}
