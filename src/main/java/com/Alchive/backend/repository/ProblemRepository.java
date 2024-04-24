package com.Alchive.backend.repository;

import com.Alchive.backend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findByUserUserId(Long userId);

    List<Problem> findByUserUserIdAndProblemPlatform(Long userId, String problemPlatform);

    Problem findByUserUserIdAndProblemNumberAndProblemPlatform(Long userId, int problemNumber, String problemplatform);

    @Query("SELECT p FROM Problem p WHERE p.user.userId = :userId AND CAST(p.problemNumber AS string) LIKE %:number%")
    List<Problem> findByUserIdAndProblemNumberContaining(@Param("userId") Long userId, @Param("number") String number);

    @Query("SELECT p FROM Problem p WHERE p.user.userId = :userId AND p.problemTitle LIKE %:keyword%")
    List<Problem> findByUserIdAndProblemTitleContaining(@Param("userId") Long userId, @Param("keyword") String keyword);

    @Query("SELECT p FROM Problem p WHERE p.user.userId = :userId AND (CAST(p.problemNumber AS string) LIKE %:keyword% OR p.problemTitle LIKE %:keyword%)")
    List<Problem> findByUserIdAndProblemNumberOrTitleContaining(@Param("userId") Long userId, @Param("keyword") String keyword);
}
