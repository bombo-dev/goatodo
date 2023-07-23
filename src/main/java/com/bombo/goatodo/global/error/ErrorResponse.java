package com.bombo.goatodo.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.validation.FieldError;

import java.util.List;

@Builder
public record ErrorResponse(
        String error,
        String status,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ValidationError> errors
) {

    private static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .error(errorCode.name())
                .status(errorCode.getErrorCode().name())
                .message(errorCode.getErrorMessage())
                .build();
    }

    @Builder
    public record ValidationError(
            String field,
            String message
    ) {
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
