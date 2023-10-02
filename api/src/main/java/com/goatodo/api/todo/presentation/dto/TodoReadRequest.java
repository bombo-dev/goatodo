package com.goatodo.api.todo.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record TodoReadRequest(
        @NotNull(message = "todoId는 null 일 수 없습니다.")
        Long todoId,

        @NotNull(message = "memberId는 null 일 수 없습니다.")
        Long memberId
) {
}
