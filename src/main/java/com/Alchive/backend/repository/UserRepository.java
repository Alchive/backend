package com.Alchive.backend.repository;

import com.Alchive.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);
}
