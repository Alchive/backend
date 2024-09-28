package com.Alchive.backend.repository;

import com.Alchive.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String userEmail);

    boolean existsByName(String userName);

    Optional<User> findByEmail(String userEmail);
}
