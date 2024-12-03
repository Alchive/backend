package com.Alchive.backend.repository;

import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnsReporitory extends JpaRepository<Sns, Long> {
    Optional<Sns> findById(Long id);
    Optional<Sns> findByUser_IdAndCategory(Long userId, SnsCategory category);

    Boolean existsByUser_IdAndCategory(Long userId, SnsCategory category);
}
