package com.Alchive.backend.repository;

import com.Alchive.backend.domain.AlgorithmProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlgorithmProblemRepository extends JpaRepository<AlgorithmProblem, Long> {
    // 추가적인 메서드가 필요한 경우 여기에 작성
}
