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
    private String accessToken;
    private String refreshToken;

    public UserResponseDTO(User user, String accessToken, String refreshToken) {
        this.userId=user.getUserId();
        this.userEmail=user.getUserEmail();
        this.userName=user.getUserName();
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
}
