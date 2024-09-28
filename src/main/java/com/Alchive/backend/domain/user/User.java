package com.Alchive.backend.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "email", length = 300, nullable = false)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "autoSave", nullable = false)
    @ColumnDefault("true")
    private Boolean autoSave = true;

    public User(Long userId) {
        this.id = userId;
    }

    @Builder // 빌더 패턴 구현
    public User(String userEmail, String userNickName) {
        this.email = userEmail;
        this.name = userNickName;
        this.createdAt = LocalDateTime.now(); // 현재 시간
    }

    // 단위 테스트용 빌더 패턴 구현
    @Builder(builderMethodName = "userTestBuilder")
    public User(Long userId, String userEmail, String userNickName) {
        this.id = userId;
        this.email = userEmail;
        this.name = userNickName;
        this.createdAt = LocalDateTime.now(); // 현재 시간
    }

    public User update(String userDescription, Boolean autoSave) { // 프로필 수정 시 사용
        this.description = userDescription;
        this.autoSave = autoSave;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

}