package com.goatodo.common.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class DuplicateException extends RestApiException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
