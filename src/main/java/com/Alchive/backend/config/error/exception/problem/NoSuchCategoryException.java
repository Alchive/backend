package com.Alchive.backend.config.error.exception.problem;

import com.Alchive.backend.config.error.ErrorCode;
import com.Alchive.backend.config.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class NoSuchCategoryException extends BusinessException {
    public NoSuchCategoryException(String category) {
        super(ErrorCode.CATEGORY_INVALID, "category", category);
    }
}
