package com.Alchive.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", columnDefinition = "INT")
    private Long userId;

    @Column(name = "userEmail", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Column(name = "userDescription")
    private String userDescription;

    @Column(name = "autoSave", nullable = false)
    private Boolean autoSave = true;

    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    public User(Long userId) {
        this.userId = userId;
    }

    @Builder // 빌더 패턴 구현
    public User(String userEmail, String userNickName) {
        this.userEmail = userEmail;
        this.userName = userNickName;
        this.createdAt = new Date(); // 현재 시간
    }

    public User update(String userDescription, Boolean autoSave) { // 프로필 수정 시 사용
        this.userDescription = userDescription;
        this.autoSave = autoSave;
        this.updatedAt = new Date();

        return this;
    }

}