package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoSuchPlatformException extends BusinessException {

    public NoSuchPlatformException(String platform) {
        super(ErrorCode.PLATFORM_INVALID, " - platform: " + platform);
    }
}
