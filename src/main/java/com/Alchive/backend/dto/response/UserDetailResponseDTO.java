package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserDetailResponseDTO {
    private String userEmail;
    private String userName;
    private String userDescription;
    private boolean autoSave;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDetailResponseDTO(User user) {
        this.userEmail=user.getEmail();
        this.userName=user.getName();
        this.userDescription=user.getDescription();
        this.autoSave=user.getAutoSave();
        this.createdAt=user.getCreatedAt();
        this.updatedAt=user.getUpdatedAt();
    }
}
