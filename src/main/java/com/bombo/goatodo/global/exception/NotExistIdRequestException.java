package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class NotExistIdRequestException extends IllegalArgumentException {

    private ErrorCode errorCode;

    public NotExistIdRequestException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
