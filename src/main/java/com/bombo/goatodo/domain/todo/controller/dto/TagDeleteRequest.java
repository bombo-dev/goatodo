package com.bombo.goatodo.domain.todo.controller.dto;

import jakarta.validation.constraints.NotNull;

public record TagDeleteRequest(
        @NotNull(message = "태그 id는 null 일 수 없습니다.")
        Long id,

        @NotNull(message = "memberId는 null 일 수 없습니다.")
        Long memberId
) {
}
