package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class RoleException extends IllegalStateException {

    private ErrorCode errorCode;

    public RoleException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
