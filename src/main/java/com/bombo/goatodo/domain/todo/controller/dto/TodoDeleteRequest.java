package com.bombo.goatodo.domain.todo.controller.dto;

import jakarta.validation.constraints.NotNull;

public record TodoDeleteRequest(
        @NotNull(message = "todoId는 null 일 수 없습니다.")
        Long todoId,

        @NotNull(message = "memberId는 null 일 수 없습니다.")
        Long memberId
) {
}
