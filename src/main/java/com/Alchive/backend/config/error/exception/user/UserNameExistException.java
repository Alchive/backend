package com.Alchive.backend.config.error.exception.user;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class UserNameExistException extends BusinessException {
    public UserNameExistException() {
        super(ErrorCode.USER_NAME_EXISTS);
    }
}
