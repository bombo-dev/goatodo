package com.bombo.goatodo.global.exception;

import com.bombo.goatodo.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {

    private ErrorCode errorCode;

    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
