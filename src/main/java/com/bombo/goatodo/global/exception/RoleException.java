package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class RoleException extends RestApiException {

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
