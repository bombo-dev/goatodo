package com.goatodo.api.todo.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record TagDeleteRequest(
        @NotNull(message = "memberId는 null 일 수 없습니다.")
        Long memberId
) {
}
