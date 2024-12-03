package com.Alchive.backend.config.error.exception.sns;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class NoSuchDiscordUserException extends BusinessException {
    public NoSuchDiscordUserException() { super(ErrorCode.DISCORD_USER_NOT_FOUND); }
}
