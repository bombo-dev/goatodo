package com.goatodo.common.exception;

import com.bombo.goatodo.global.error.ErrorCode;

public class RoleException extends RestApiException {

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
