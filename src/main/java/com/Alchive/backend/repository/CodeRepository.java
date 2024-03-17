package com.Alchive.backend.repository;

import com.Alchive.backend.domain.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    // 추가적인 코드 관련 메소드가 필요한 경우 여기에 추가할 수 있습니다.
}