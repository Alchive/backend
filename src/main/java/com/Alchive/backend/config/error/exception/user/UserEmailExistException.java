package com.Alchive.backend.config.error.exception.user;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserEmailExistException extends BusinessException {
    public UserEmailExistException(String userEmail) {
        super(ErrorCode.USER_EMAIL_EXISTS, "userEmail", userEmail);
    }
}
