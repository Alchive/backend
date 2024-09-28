package com.Alchive.backend.repository;

import com.Alchive.backend.domain.algorithm.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long> {
    boolean existsByName(String name);

    Algorithm findByName(String name);
}
