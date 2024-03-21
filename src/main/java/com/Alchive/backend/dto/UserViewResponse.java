package com.Alchive.backend.dto;

import com.Alchive.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserViewResponse {
    private Long userId;
    private String userEmail;
    private String userNickName;
    private String userDescription;
    private boolean autoSave;
    private Date createdAt;
    private Date updatedAt;

    public UserViewResponse(User user) {
        this.userId= user.getUserId();
        this.userEmail=user.getUserEmail();
        this.userNickName=user.getUserNickName();
        this.userDescription=user.getUserDescription();
        this.createdAt=user.getCreatedAt();
        this.updatedAt=user.getUpdatedAt();
    }
}
