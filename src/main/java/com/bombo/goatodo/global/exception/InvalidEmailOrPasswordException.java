package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class InvalidEmailOrPasswordException extends IllegalArgumentException {

    private ErrorCode errorCode;

    public InvalidEmailOrPasswordException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
