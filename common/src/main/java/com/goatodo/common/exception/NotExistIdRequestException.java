package com.goatodo.common.exception;


import com.goatodo.common.error.ErrorCode;

public class NotExistIdRequestException extends RestApiException {


    public NotExistIdRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
