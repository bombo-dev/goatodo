package com.goatodo.api.advice;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RestApiException;
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
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> InternalServerErrorException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.of(errorCode);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(
            MethodArgumentNotValidException e,
            ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatusCode())
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
                .status(errorCode.getStatusCode())
                .message(errorCode.getErrorMessage())
                .errors(validationErrors)
                .build();
    }
}
