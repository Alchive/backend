package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserDetailResponseDTO {
    private String userEmail;
    private String userName;
    private String userDescription;
    private boolean autoSave;
    private Date createdAt;
    private Date updatedAt;

    public UserDetailResponseDTO(User user) {
        this.userEmail=user.getUserEmail();
        this.userName=user.getUserName();
        this.userDescription=user.getUserDescription();
        this.autoSave=user.getAutoSave();
        this.createdAt=user.getCreatedAt();
        this.updatedAt=user.getUpdatedAt();
    }
}
