package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponseDTO {
    private Long userId;
    private String userEmail;
    private String userName;

    public UserResponseDTO(User user) {
        this.userId=user.getUserId();
        this.userEmail=user.getUserEmail();
        this.userName=user.getUserName();
    }
}
