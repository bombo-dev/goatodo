package com.bombo.goatodo.global.handler;

import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.error.ErrorResponse;
import com.bombo.goatodo.global.exception.RestApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_ARGUMENT;
        return handleExceptionInternal(e, errorCode);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(RestApiException e) {
        return ResponseEntity.status(e.getErrorCode().getErrorCode())
                .body(makeErrorResponse(e.getErrorCode()));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .error(errorCode.name())
                .message(errorCode.getErrorMessage())
                .status(errorCode.getErrorCode().name())
                .build();
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(
            MethodArgumentNotValidException e,
            ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getErrorCode())
                .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(
            MethodArgumentNotValidException e,
            ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .toList();

        return ErrorResponse.builder()
                .error(errorCode.name())
                .status(errorCode.getErrorCode().name())
                .message(errorCode.getErrorMessage())
                .errors(validationErrors)
                .build();
    }
}
