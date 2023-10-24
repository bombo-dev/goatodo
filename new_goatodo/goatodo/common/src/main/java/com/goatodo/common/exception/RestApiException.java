package com.goatodo.common.exception;

import com.goatodo.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {

    private ErrorCode errorCode;

    public RestApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
