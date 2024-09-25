package com.Alchive.backend.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "isDeleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Column(name = "email", length = 300, nullable = false)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "autoSave", nullable = false)
    @ColumnDefault("true")
    private Boolean autoSave;

    public User(Long userId) {
        this.id = userId;
    }

    @Builder // 빌더 패턴 구현
    public User(String userEmail, String userNickName) {
        this.email = userEmail;
        this.name = userNickName;
        this.createdAt = new Date(); // 현재 시간
    }

    // 단위 테스트용 빌더 패턴 구현
    @Builder(builderMethodName = "userTestBuilder")
    public User(Long userId, String userEmail, String userNickName) {
        this.id = userId;
        this.email = userEmail;
        this.name = userNickName;
        this.createdAt = new Date(); // 현재 시간
    }

    public User update(String userDescription, Boolean autoSave) { // 프로필 수정 시 사용
        this.description = userDescription;
        this.autoSave = autoSave;
        this.updatedAt = new Date();

        return this;
    }

}