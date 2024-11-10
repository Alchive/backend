package com.Alchive.backend.domain.user;

import com.Alchive.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @Column(name = "email", length = 300, nullable = false)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "auto_save", nullable = false)
    @ColumnDefault("true")
    private Boolean autoSave = true;

    public User(Long id) {
        this.id = id;
    }

    @Builder // 빌더 패턴 구현
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // 단위 테스트용 빌더 패턴 구현
    @Builder(builderMethodName = "userTestBuilder")
    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User update(String description, Boolean autoSave) { // 프로필 수정 시 사용
        this.description = description;
        this.autoSave = autoSave;
        return this;
    }
}