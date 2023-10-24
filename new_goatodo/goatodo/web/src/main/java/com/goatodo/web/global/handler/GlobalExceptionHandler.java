package com.goatodo.web.global.handler;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.web.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Error occurred", exception);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        HttpStatus httpStatus = HttpStatus.valueOf(errorCode.getStatus());
        return new ResponseEntity<>(ErrorResponse.from(errorCode), httpStatus);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        var errorCode = ErrorCode.valueOf(exception.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(errorCode.getStatus());
        return new ResponseEntity<>(ErrorResponse.from(errorCode), httpStatus);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {
        var errorCode = ErrorCode.INVALID_ARGUMENT;
        HttpStatus httpStatus = HttpStatus.valueOf(errorCode.getStatus());
        return new ResponseEntity<>(ErrorResponse.from(errorCode, exception), httpStatus);
    }
}
