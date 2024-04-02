package com.Alchive.backend.repository;

import com.Alchive.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByProblemPlatform(String problemPlatform);

    @Query("SELECT p FROM Problem p WHERE CAST(p.problemNumber AS string) LIKE %:number%")
    List<Problem> findByProblemNumberContaining(String number);
    List<Problem> findByProblemTitleContaining(String keyword);
}
