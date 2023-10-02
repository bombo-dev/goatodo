package com.goatodo.domain.member.exception;


import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RestApiException;

public class InvalidEmailOrPasswordException extends RestApiException {


    public InvalidEmailOrPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
