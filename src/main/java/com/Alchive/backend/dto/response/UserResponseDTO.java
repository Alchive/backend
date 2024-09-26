package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponseDTO {
    private Long userId;
    private String userEmail;
    private String userName;
    private String accessToken;
    private String refreshToken;

    public UserResponseDTO(User user, String accessToken, String refreshToken) {
        this.userId=user.getId();
        this.userEmail=user.getEmail();
        this.userName=user.getName();
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
}
