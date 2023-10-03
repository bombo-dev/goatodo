package com.goatodo.common.exception;


import com.goatodo.common.error.ErrorCode;

public class RoleException extends RestApiException {

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
