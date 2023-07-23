package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class NotExistIdRequestException extends RestApiException {


    public NotExistIdRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
