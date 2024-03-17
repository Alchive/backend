package com.Alchive.backend.repository;

import com.Alchive.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 추가적인 사용자 관련 메소드가 필요한 경우 여기에 추가할 수 있습니다.
}
