package com.Alchive.backend.repository;

import com.Alchive.backend.domain.AlgorithmProblem;
import com.Alchive.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmProblemRepository extends JpaRepository<AlgorithmProblem, Long> {
    List<AlgorithmProblem> findByProblem(Problem problem);

}
