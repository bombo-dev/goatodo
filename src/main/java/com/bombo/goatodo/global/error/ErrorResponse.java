package com.bombo.goatodo.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
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

    @Getter
    @Builder
    @RequiredArgsConstructor
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
