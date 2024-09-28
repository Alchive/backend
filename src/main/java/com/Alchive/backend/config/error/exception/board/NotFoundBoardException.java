package com.Alchive.backend.config.error.exception.board;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;

public class NotFoundBoardException extends BusinessException {
    public NotFoundBoardException() {
        super(ErrorCode.BOARD_NOT_FOUND);
    }
}
