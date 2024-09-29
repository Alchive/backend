package com.Alchive.backend.repository;

import com.Alchive.backend.domain.solution.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findAllByBoard_Id(Long boardId);
}
