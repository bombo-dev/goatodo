package com.goatodo.common.exception;


import com.goatodo.common.error.ErrorCode;

public class DuplicateException extends RestApiException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
