package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserViewResponse {
    private int code;
    private String message;
    private UserDetailData data;

    public UserViewResponse(User user) {
        this.code=200;
        this.message="유저 정보를 불러왔습니다. ";
        this.data= new UserDetailData(user);
    }
}
