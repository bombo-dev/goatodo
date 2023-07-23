package com.bombo.goatodo.domain.member.exception;

import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.RestApiException;

public class InvalidEmailOrPasswordException extends RestApiException {


    public InvalidEmailOrPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
