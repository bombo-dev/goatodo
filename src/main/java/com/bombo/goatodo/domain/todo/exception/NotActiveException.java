package com.bombo.goatodo.domain.todo.exception;

import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.RestApiException;

public class NotActiveException extends RestApiException {

    public NotActiveException(ErrorCode errorCode) {
        super(errorCode);
    }
}
