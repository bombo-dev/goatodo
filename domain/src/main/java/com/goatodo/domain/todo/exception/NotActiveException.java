package com.goatodo.domain.todo.exception;


import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RestApiException;

public class NotActiveException extends RestApiException {

    public NotActiveException(ErrorCode errorCode) {
        super(errorCode);
    }
}
