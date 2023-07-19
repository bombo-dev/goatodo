package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class DuplicateException extends IllegalArgumentException {

    private ErrorCode errorCode;

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
