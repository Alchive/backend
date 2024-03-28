package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserDetailData {
    private String userEmail;
    private String userNickName;
    private String userDescription;
    private boolean autoSave;
    private Date createdAt;
    private Date updatedAt;

    public UserDetailData(User user) {
        this.userEmail=user.getUserEmail();
        this.userNickName=user.getUserNickName();
        this.userDescription=user.getUserDescription();
        this.autoSave=user.getAutoSave();
        this.createdAt=user.getCreatedAt();
        this.updatedAt=user.getUpdatedAt();
    }
}
