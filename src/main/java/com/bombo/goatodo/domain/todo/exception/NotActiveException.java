package com.bombo.goatodo.domain.todo.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class NotActiveException extends IllegalStateException {

    private ErrorCode errorCode;

    public NotActiveException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
